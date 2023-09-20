import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ticket {
    private int id;
    private Date fecha;
    private List<Producto> productosComprados = new ArrayList<>();

    public Ticket() {
        this.fecha = new Date();
    }

    public Ticket(int id, Date fecha) {
        this.id = id;
        this.fecha = fecha;
    }


    public void añadirProducto(Producto producto) {
        productosComprados.add(producto);
    }

    public double calcularTotal() {
        return productosComprados.stream().mapToDouble(Producto::getPrecio).sum();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ticket ID: ").append(id).append("\n");
        sb.append("Fecha: ").append(fecha).append("\n");
        for (Producto producto : productosComprados) {
            sb.append(producto.toString()).append("\n");
        }
        sb.append("Total: ").append(calcularTotal());
        return sb.toString();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFecha() {
        return fecha;
    }

    public List<Producto> getProductosComprados() {
        return new ArrayList<>(productosComprados);
    }
}
