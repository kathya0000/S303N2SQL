import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3307/floristeria";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection = null;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontró el driver de MySQL", e);
        }
    }

    private Database() {  // Constructor privado para evitar instancias adicionales

    }

    public static Connection getConnection() {
        if (connection == null) {
            synchronized (Database.class) {
                if (connection == null) {
                    try {
                        connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    } catch (SQLException e) {
                        throw new RuntimeException("Error al conectar a la base de datos.", e);
                    }
                }
            }
        }
        return connection;
    }
}
