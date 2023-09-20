import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {
    private final Connection conn;
    private final ArbolDAO arbolDAO;
    private final FlorDAO florDAO;
    private final DecoracionDAO decoracionDAO;

    private final String url = "jdbc:mysql://localhost:3307/floristeria";
    private final String user = "root";
    private final String password = "";

    public TicketDAO(Connection conn, ArbolDAO arbolDAO, FlorDAO florDAO, DecoracionDAO decoracionDAO) {
        this.conn = conn;
        this.arbolDAO = arbolDAO;
        this.florDAO = florDAO;
        this.decoracionDAO = decoracionDAO;
    }

    public Ticket obtener(int id) throws Exception {
        String query = "SELECT * FROM tickets WHERE id=?";
        Ticket ticket = new Ticket();

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                ticket.setId(rs.getInt("id"));
                ticket.setFecha(rs.getDate("fecha"));
            }

            query = "SELECT * FROM ticket_items WHERE ticket_id=?";
            pst.setInt(1, id);
            rs = pst.executeQuery();

            while (rs.next()) {
                String tipoProducto = rs.getString("tipo_producto");
                int productoId = rs.getInt("producto_id");
                Producto producto = null;

                switch (tipoProducto) {
                    case "arbol":
                        producto = arbolDAO.get(productoId);
                        break;
                    case "flor":
                        producto = florDAO.get(productoId);
                        break;
                    case "decoracion":
                        producto = decoracionDAO.get(productoId);
                        break;
                }

                if (producto != null) {
                    ticket.añadirProducto(producto);
                }
            }
        }
        return ticket;
    }
   public int guardar(Ticket ticket) throws Exception {
       String query = "INSERT INTO tickets (fecha, total) VALUES (?, ?)";
       int generatedId = 0;  // Variable para almacenar el ID generado

       try (Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

           pst.setDate(1, new java.sql.Date(ticket.getFecha().getTime()));
           pst.setDouble(2, ticket.calcularTotal());
           pst.executeUpdate();

           ResultSet generatedKeys = pst.getGeneratedKeys();
           if (generatedKeys.next()) {
               generatedId = generatedKeys.getInt(1);  // Almacena el ID generado
               guardarProductosEnTicket(ticket.getProductosComprados(), generatedId);
           }
       }
       return generatedId;  // Devuelve el ID generado
   }


    private void guardarProductosEnTicket (List< Producto > productos, int ticketId) throws Exception {
        String query = "INSERT INTO ticket_items (ticket_id, tipo_producto, producto_id, precio_unitario) VALUES (?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(query)) {

            for (Producto producto : productos) {
                pst.setInt(1, ticketId);
                if (producto instanceof Arbol) {
                    pst.setString(2, "arbol");
                } else if (producto instanceof Flor) {
                    pst.setString(2, "flor");
                } else if (producto instanceof Decoracion) {
                    pst.setString(2, "decoracion");
                }
                pst.setInt(3, producto.getId());
                pst.setDouble(4, producto.getPrecio());
                pst.addBatch();
            }

            pst.executeBatch();
        }
    }
    public void actualizar (Ticket ticket) throws Exception {
        String query = "UPDATE tickets SET fecha = ?, total = ? WHERE id = ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setDate(1, new java.sql.Date(ticket.getFecha().getTime()));
            pst.setDouble(2, ticket.calcularTotal());
            pst.setInt(3, ticket.getId());
            pst.executeUpdate();

            // Ahora, eliminamos todos los productos relacionados a este ticket en la tabla 'ticket_items'
            query = "DELETE FROM ticket_items WHERE ticket_id = ?";
            try (PreparedStatement pstDelete = conn.prepareStatement(query)) {
                pstDelete.setInt(1, ticket.getId());
                pstDelete.executeUpdate();
            }

            // Luego, reinsertamos los productos del ticket
            guardarProductosEnTicket(ticket.getProductosComprados(), ticket.getId());
        }
    }

   public List<Ticket> obtenerTodosLosTickets() throws SQLException {
       List<Ticket> listaTickets = new ArrayList<>();
       String query = "SELECT * FROM tickets";
       try (PreparedStatement pst = conn.prepareStatement(query)) {
           ResultSet rs = pst.executeQuery();
           while (rs.next()) {
               Ticket ticket = new Ticket();
               ticket.setId(rs.getInt("id"));
               ticket.setFecha(rs.getDate("fecha"));

               // Recuperar los productos asociados a este ticket
               String queryProductos = "SELECT * FROM ticket_items WHERE ticket_id = ?";
               try (PreparedStatement pstProductos = conn.prepareStatement(queryProductos)) {
                   pstProductos.setInt(1, ticket.getId());
                   ResultSet rsProductos = pstProductos.executeQuery();
                   while (rsProductos.next()) {
                       int productoId = rsProductos.getInt("producto_id");
                       String tipoProducto = rsProductos.getString("tipo_producto");
                       Producto producto = null;
                       switch (tipoProducto) {
                           case "arbol":
                               producto = arbolDAO.get(productoId);
                               break;
                           case "flor":
                               producto = florDAO.get(productoId);
                               break;
                           case "decoracion":
                               producto = decoracionDAO.get(productoId);
                               break;
                       }
                       if (producto != null) {
                           ticket.añadirProducto(producto);
                       }
                   }
               }

               listaTickets.add(ticket);
           }
       }
       return listaTickets;
   }




    public void eliminar ( int id) throws Exception {
        try (Connection con = DriverManager.getConnection(url, user, password)) {

            // Primero, eliminamos todos los productos relacionados con el ticket en la tabla 'ticket_items'
            String query = "DELETE FROM ticket_items WHERE ticket_id = ?";
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setInt(1, id);
                pst.executeUpdate();
            }

            // Luego, eliminamos el ticket
            query = "DELETE FROM tickets WHERE id = ?";
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setInt(1, id);
                pst.executeUpdate();
            }
        }
    }
}
