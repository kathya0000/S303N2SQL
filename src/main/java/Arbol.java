

class Arbol extends Producto {
    private double altura;

    public Arbol(String nombre, double altura, double precio) {
        super(nombre, precio);
        this.altura = altura;
    }
    public Arbol() {
        super(); // Llama al constructor de la clase base (Producto) sin argumentos
        this.nombre = "Sin nombre";
        this.altura = 0.0;
        this.precio = 0.0;
    }

    public double getAltura() {
        return altura;
    }

    @Override
    public String getNombre() {
        return super.getNombre();
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }


    @Override
    public void setNombre(String nombre) {
        super.setNombre(nombre);
    }

    @Override
    public String toString() {

        return "Árbol con altura de " + altura + " metros. Precio: " + precio;
    }



}
