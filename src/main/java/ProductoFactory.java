

public class ProductoFactory {

    public static Producto crearProductoArbol(String nombre, double altura, double precio) {
        return new Arbol(nombre, altura, precio);
    }

    public static Producto crearProductoFlor(String nombre, String color, double precio) {
        return new Flor(nombre, color, precio);
    }

    public static Producto crearProductoDecoracion(String nombre, String material, double precio) {
        if (!material.equalsIgnoreCase("madera") && !material.equalsIgnoreCase("plastico")) {
            throw new IllegalArgumentException("Material inválido. Debe ser 'madera' o 'plastico'.");
        }
        return new Decoracion(nombre, material, precio);
    }
}
