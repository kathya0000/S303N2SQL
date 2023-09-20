import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DecoracionDAO extends ProductoDAO {
    public DecoracionDAO(Connection conn) {
        super(conn);
    }

    @Override
    protected String getTableName() {
        return "decoracion";
    }

    @Override
    protected Decoracion mapResultSetToProducto(ResultSet rs) throws SQLException {
        String nombre = rs.getString("nombre");
        String material = rs.getString("material");
        double precio = rs.getDouble("precio");
        Decoracion decoracion = new Decoracion(nombre, material, precio);
        decoracion.setId(rs.getInt("id"));
        return decoracion;
    }
    @Override
    public void add(Producto producto) {
        if (!(producto instanceof Decoracion)) {
            throw new IllegalArgumentException("Se esperaba un objeto de tipo Decoracion");
        }

        Decoracion decoracion = (Decoracion) producto;
        String sql = "INSERT INTO decoracion (nombre, material, precio) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, decoracion.getNombre());
            stmt.setString(2, decoracion.getMaterial());
            stmt.setDouble(3, decoracion.getPrecio());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    decoracion.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al añadir decoración.", e);
        }
    }

    @Override
    public Producto get(int id) {
        return super.get(id);
    }

    @Override
    public Producto getByName(String nombre) {
        return super.getByName(nombre);
    }

    public List<Decoracion> getAll() {
        List<Decoracion> decoraciones = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                decoraciones.add(mapResultSetToProducto(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la lista de decoraciones.", e);
        }
        return decoraciones;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el decoración.", e);
        }
    }
}