import java.util.ArrayList;
import java.util.List;

public class Floristeria {
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

    public void mostrarStock() {
        for (Producto producto : productos) {
            System.out.println(producto.toString());
        }
    }

    public Producto obtenerProducto(int id) {
        System.out.println("Buscando producto con ID: " + id);
        for (Producto producto : productos) {
            if (producto.getId() == id) {
                return producto;
            }
        }
        return null;
    }

    public List<Producto> obtenerProductos() {
        return productos;
    }

    public double valorTotal() {
        return productos.stream().mapToDouble(Producto::getPrecio).sum();
    }

    public void añadirTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void mostrarComprasAntiguas() {
        tickets.forEach(System.out::println);
    }

    public double totalDineroGanado() {
        return tickets.stream().mapToDouble(Ticket::calcularTotal).sum();
    }
}
