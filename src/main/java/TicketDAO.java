import java.sql.*;
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
    public void guardar (Ticket ticket) throws Exception {
        String query = "INSERT INTO tickets (fecha, total) VALUES (?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pst.setDate(1, new java.sql.Date(ticket.getFecha().getTime()));
            pst.setDouble(2, ticket.calcularTotal());
            pst.executeUpdate();

            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                int ticketId = generatedKeys.getInt(1);
                guardarProductosEnTicket(ticket.getProductosComprados(), ticketId);
            }
        }
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
