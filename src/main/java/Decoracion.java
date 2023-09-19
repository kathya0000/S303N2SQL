


class Decoracion extends Producto {
    private String material;

    public Decoracion(String nombre, String material, double precio) {
        super(nombre, precio);
        this.material = material;
    }
    public Decoracion() {
        super();
        this.material = "Sin material";
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        if(!material.equals("madera") && !material.equals("plastico")) {
            throw new IllegalArgumentException("Material inválido");
        }
        this.material = material;
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
        return "Decoración de material " + material + ". Precio: " + precio;
    }
}
