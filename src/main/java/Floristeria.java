
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Floristeria {
    private ProductoDAO arbolDAO;
    private ProductoDAO florDAO;
    private ProductoDAO decoracionDAO;

    private static Floristeria instancia = null;
    private List<Producto> productos = new ArrayList<>();
    private List<Ticket> tickets = new ArrayList<>();

    private Floristeria() {   // Constructor privado

    }

    public static Floristeria obtenerInstancia() {
        if (instancia == null) {
            instancia = new Floristeria();
        }
        return instancia;
    }


    public void añadirProducto(Producto producto) {
        System.out.println("Añadiendo producto con ID: " + producto.getId());
        productos.add(producto);
    }

    public void eliminarProducto(int id) {
        productos.removeIf(p -> p.getId() == id);
    }

    public void mostrarStock(ProductoDAO arbolDAO, ProductoDAO florDAO, ProductoDAO decoracionDAO) {
        for (Producto arbol : arbolDAO.getAll()) {
            System.out.println(arbol.toString());
        }
        for (Producto flor : florDAO.getAll()) {
            System.out.println(flor.toString());
        }
        for (Producto decoracion : decoracionDAO.getAll()) {
            System.out.println(decoracion.toString());
        }
    }

    public Producto obtenerProducto(int id, ProductoDAO arbolDAO, ProductoDAO florDAO, ProductoDAO decoracionDAO) {
        System.out.println("Buscando producto con ID: " + id);

        Producto producto = arbolDAO.get(id);
        if (producto != null) {
            return producto;
        }

        producto = florDAO.get(id);
        if (producto != null) {
            return producto;
        }

        producto = decoracionDAO.get(id);
        if (producto != null) {
            return producto;
        }

        return null;
    }
    public Producto obtenerProductoPorNombre(String nombre, ProductoDAO arbolDAO, ProductoDAO florDAO, ProductoDAO decoracionDAO) {
        // Buscar en la tabla de árboles
        Producto producto = arbolDAO.getByName(nombre);
        if (producto != null) {
            return producto;
        }

        // Buscar en la tabla de flores
        producto = florDAO.getByName(nombre);
        if (producto != null) {
            return producto;
        }

        // Buscar en la tabla de decoraciones
        producto = decoracionDAO.getByName(nombre);
        if (producto != null) {
            return producto;
        }

        return null; // Si no se encuentra el producto en ninguna tabla
    }



    public List<Producto> obtenerProductos() {
        return productos;
    }

    public double valorTotal(ProductoDAO arbolDAO, ProductoDAO florDAO, ProductoDAO decoracionDAO) {
        double valorArboles = arbolDAO.getAll().stream().mapToDouble(Producto::getPrecio).sum();
        double valorFlores = florDAO.getAll().stream().mapToDouble(Producto::getPrecio).sum();
        double valorDecoraciones = decoracionDAO.getAll().stream().mapToDouble(Producto::getPrecio).sum();

        return valorArboles + valorFlores + valorDecoraciones;
    }


    public void añadirTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void mostrarComprasAntiguas() {
        System.out.println("Número de tickets: " + tickets.size());
        tickets.forEach(System.out::println);
    }


    public double totalDineroGanado() {
       // Imprimir el número de tickets en la lista para depuración
       System.out.println("Número de tickets: " + tickets.size());

       // Imprimir el total de cada ticket para depuración
       tickets.forEach(ticket -> System.out.println("Total del ticket " + ticket.getId() + ": " + ticket.calcularTotal()));

       return tickets.stream().mapToDouble(Ticket::calcularTotal).sum();
   }
    public void actualizarTicketsDesdeBaseDeDatos(TicketDAO ticketDAO) throws SQLException {
        this.tickets = ticketDAO.obtenerTodosLosTickets();
    }


}
