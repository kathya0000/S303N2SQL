public abstract class Producto {
    protected int id;
    protected String nombre;
    protected double precio;

    public Producto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }
    public Producto() {
        this.nombre = "Sin nombre"; // Valor por defecto para el nombre
        this.precio = 0.0;
    }

    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public abstract String toString();
}

