import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Connection connection = Database.getConnection();// Establecer la conexión aquí

        ArbolDAO arbolDAO = new ArbolDAO(connection);
        FlorDAO florDAO = new FlorDAO(connection);
        DecoracionDAO decoracionDAO = new DecoracionDAO(connection);

        // Aquí creas la instancia de TicketDAO
        TicketDAO ticketDAO = new TicketDAO(connection, arbolDAO, florDAO, decoracionDAO);

        Scanner scanner = new Scanner(System.in);
        Floristeria floristeria = Floristeria.obtenerInstancia();

        int opcion;

        do {
            System.out.println("\n--- Floristería ---");
            System.out.println("1. Añadir producto");
            System.out.println("2. Retirar producto");
            System.out.println("3. Mostrar stock");
            System.out.println("4. Mostrar valor total de la floristería");
            System.out.println("5. Crear ticket de compra");
            System.out.println("6. Mostrar lista de compras antiguas");
            System.out.println("7. Visualizar total de dinero ganado");
            System.out.println("8. Guardar");
            System.out.println("0. Salir");

            System.out.print("Elige una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    añadirProducto(scanner, floristeria, arbolDAO, florDAO, decoracionDAO);
                    break;

                case 2:
                    retirarProducto(scanner, floristeria, arbolDAO, florDAO, decoracionDAO);
                    break;

                case 3:
                    floristeria.mostrarStock(arbolDAO, florDAO, decoracionDAO);
                    break;

                case 4:
                    System.out.println("Valor total: " + floristeria.valorTotal(arbolDAO, florDAO, decoracionDAO));
                    break;

                /*case 5:
                    try {
                        crearTicketCompra(scanner, floristeria, connection, arbolDAO, florDAO, decoracionDAO);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;*/

                case 6:
                    floristeria.mostrarComprasAntiguas();
                    break;

                case 7:
                    System.out.println("Total de dinero ganado: " + floristeria.totalDineroGanado());
                    break;
                case 8 :
                    try {
                        guardarEnBaseDeDatos(floristeria);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;


                case 0:
                    System.out.println("Gracias por usar la aplicación de la Floristería.");
                    break;

                default:
                    System.out.println("Opción no reconocida.");
            }
        } while (opcion != 0);
    }

    private static void añadirProducto(Scanner scanner, Floristeria floristeria, ProductoDAO arbolDAO, ProductoDAO florDAO, ProductoDAO decoracionDAO) {
        System.out.print("Tipo de producto (arbol, flor, decoracion): ");
        String tipo = scanner.next();
        Producto producto;

        switch (tipo) {
            case "arbol":
                System.out.print("Nombre del arbol: ");
                String nombreArbol = scanner.next();
                System.out.print("Altura del arbol: ");
                double altura = scanner.nextDouble();
                System.out.print("Precio del arbol: ");
                double precioArbol = scanner.nextDouble();
                producto = ProductoFactory.crearProductoArbol(nombreArbol, altura, precioArbol);

                // Crear un nuevo objeto Arbol y asignarle los valores proporcionados por el usuario
                Arbol arbol = new Arbol();
                arbol.setNombre(nombreArbol);
                arbol.setAltura(altura);
                arbol.setPrecio(precioArbol);

                // Agregar el árbol a la floristería
                floristeria.añadirProducto(arbol);

                // Luego, guarda el objeto en la base de datos utilizando el DAO correspondiente
                arbolDAO.add(arbol);

                break;
            case "flor":
                System.out.print("Nombre del flor: ");
                String nombreFlor = scanner.next();
                System.out.print("Color de la flor: ");
                String color = scanner.next();
                System.out.print("Precio de la flor: ");
                double precioFlor = scanner.nextDouble();
                producto = ProductoFactory.crearProductoFlor(nombreFlor, color, precioFlor);

                Flor flor = new Flor();
                flor.setNombre(nombreFlor);
                flor.setColor(color);
                flor.setPrecio(precioFlor);

                // Agregar la flor a la floristería
                floristeria.añadirProducto(flor);

                // Luego, guarda el objeto en la base de datos utilizando el DAO correspondiente
                florDAO.add(flor);

                break;
            case "decoracion":
                System.out.print("Nombre de la decoracion: ");
                String nombreDecoracion = scanner.next();
                System.out.print("Tipo de material (madera/plastico): ");
                String material = scanner.next();
                System.out.print("Precio de la decoración: ");
                double precioDecoracion = scanner.nextDouble();
                producto = ProductoFactory.crearProductoDecoracion(nombreDecoracion,material, precioDecoracion);

                Decoracion decoracion = new Decoracion();
                decoracion.setNombre(nombreDecoracion);
                decoracion.setMaterial(material);
                decoracion.setPrecio(precioDecoracion);

                // Agregar la decoración a la floristería
                floristeria.añadirProducto(decoracion);

                // Luego, guarda el objeto en la base de datos utilizando el DAO correspondiente
                decoracionDAO.add(decoracion);

                break;
            default:
                System.out.println("Tipo de producto no reconocido.");
                return; // Salimos del método si el tipo no es reconocido
        }

        floristeria.añadirProducto(producto);
    }

    private static void  retirarProducto( Scanner scanner, Floristeria floristeria, ProductoDAO arbolDAO, ProductoDAO florDAO, ProductoDAO decoracionDAO) {

        System.out.print("¿Qué tipo de producto deseas eliminar? (arbol, flor, decoracion): ");
        String tipo = scanner.next();

        System.out.print("ID del producto a eliminar: ");
        int id = scanner.nextInt();

        // Primero, intenta obtener el producto de la instancia de Floristeria
        Producto producto = floristeria.obtenerProducto(id, arbolDAO, florDAO, decoracionDAO);

        if (producto != null) {
            switch (tipo) {
                case "arbol":
                    if (producto instanceof Arbol) {
                        arbolDAO.delete(id);
                        floristeria.eliminarProducto(id);
                    } else {
                        System.out.println("El ID proporcionado no corresponde a un árbol.");
                    }
                    break;
                case "flor":
                    if (producto instanceof Flor) {
                        florDAO.delete(id);
                        floristeria.eliminarProducto(id);
                    } else {
                        System.out.println("El ID proporcionado no corresponde a una flor.");
                    }
                    break;
                case "decoracion":
                    if (producto instanceof Decoracion) {
                        decoracionDAO.delete(id);
                        floristeria.eliminarProducto(id);
                    } else {
                        System.out.println("El ID proporcionado no corresponde a una decoración.");
                    }
                    break;
                default:
                    System.out.println("Tipo de producto no reconocido.");
                    return;
            }
        } else {
            System.out.println("Producto no encontrado con ID: " + id);
        }
    }


   /* private static void crearTicketCompra(Scanner scanner, Floristeria floristeria, Connection connection, ProductoDAO arbolDAO, ProductoDAO florDAO, ProductoDAO decoracionDAO) throws Exception {
        Ticket ticket = new Ticket();
        TicketDAO ticketDAO = new TicketDAO(connection, (ArbolDAO) arbolDAO, (FlorDAO) florDAO, (DecoracionDAO) decoracionDAO);
        String respuesta;

        do {
            System.out.print("ID del producto a comprar: ");
            int idProducto = scanner.nextInt();

            int id;
            Producto productoAComprar = floristeria.obtenerProducto(id,arbolDAO, florDAO, decoracionDAO);

            if (productoAComprar != null) {
                ticket.añadirProducto(productoAComprar);
            } else {
                System.out.println("Producto no encontrado con ID: " + idProducto);
            }

            System.out.print("¿Deseas agregar otro producto al ticket? (si/no) ");
            respuesta = scanner.next();
        } while (respuesta.equalsIgnoreCase("si"));

        ticketDAO.guardar(ticket);
    }*/

    private static void guardarEnBaseDeDatos(Floristeria floristeria) {
        try {
            Connection connection = Database.getConnection();

            // Crear instancias de los DAO específicos
            ProductoDAO arbolDAO = new ArbolDAO(connection);
            ProductoDAO florDAO = new FlorDAO(connection);
            ProductoDAO decoracionDAO = new DecoracionDAO(connection);

            // Obtén la lista de productos de la floristería
            List<Producto> productos = floristeria.obtenerProductos();

            // Itera sobre la lista de productos y agrega cada producto al DAO correspondiente
            for (Producto producto : productos) {
                if (producto instanceof Arbol) {
                    arbolDAO.add(producto);
                } else if (producto instanceof Flor) {
                    florDAO.add(producto);
                } else if (producto instanceof Decoracion) {
                    decoracionDAO.add(producto);
                }
            }

            // Resto del código para guardar los tickets en la base de datos

            System.out.println("Datos guardados en la base de datos.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al guardar los datos en la base de datos.");
        }
    }


}
