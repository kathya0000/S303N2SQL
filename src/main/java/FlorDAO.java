import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlorDAO extends ProductoDAO {
    public FlorDAO(Connection conn) {
        super(conn);
    }

    @Override
    protected String getTableName() {
        return "flores";
    }

    @Override
    protected Flor mapResultSetToProducto(ResultSet rs) throws SQLException {
        String nombre = rs.getString("nombre"); // Asegúrate de que la columna en tu base de datos se llama "nombre"
        String color = rs.getString("color");
        double precio = rs.getDouble("precio");
        Flor flor = new Flor(nombre, color, precio);
        flor.setId(rs.getInt("id"));
        return flor;
    }

    @Override
    public Producto get(int id) {
        return super.get(id);
    }

    @Override
    public Producto getByName(String nombre) {
        return super.getByName(nombre);
    }

    @Override
    public List<Flor> getAll() {
        List<Flor> flores = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                flores.add(mapResultSetToProducto(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la lista de flores.", e);
        }
        return flores;
    }
    @Override
    public void add(Producto producto) {
        if (!(producto instanceof Flor )) {
            throw new IllegalArgumentException("Se esperaba un objeto de tipo Flor");
        }

        Flor flor = (Flor) producto;
        String sql = "INSERT INTO flores (nombre, color, precio) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, flor.getNombre());
            stmt.setString(2, flor.getColor());
            stmt.setDouble(3, flor.getPrecio());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    flor.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al añadir flor.", e);
        }
    }


    @Override
    public void delete(int id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el flor.", e);
        }
    }
}
