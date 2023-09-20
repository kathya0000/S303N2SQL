
class Flor extends Producto {
    private String color;

    public Flor(String nombre, int id, String color, double precio) { // Constructor con ID
        super(nombre, precio);
        this.id = id; // Asigna el ID
        this.color = color;
    }

    public Flor(String nombre, String color, double precio) { // Constructor sin ID
        super(nombre, precio);
        this.color = color;
    }
    public Flor() {
        super(); // Llama al constructor de la clase base (Producto) sin argumentos
        this.color = "Sin color"; // Valor por defecto para el color
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String getNombre() {
        return super.getNombre();
    }

    @Override
    public void setNombre(String nombre) {
        super.setNombre(nombre);
    }

    @Override
    public String toString() {
        return "Flor "   +   nombre   +   " de color "   +  color  +   ". Precio: "   +  precio;
    }
}
