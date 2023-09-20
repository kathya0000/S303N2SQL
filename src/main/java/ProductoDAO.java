import java.sql.*;
import java.util.List;

public abstract class ProductoDAO {
    protected Connection conn;



    public ProductoDAO() {
        this.conn = Database.getConnection();
    }

    public ProductoDAO(Connection conn) {
        this.conn = conn;
    }

    public void add(Producto producto) {
        String sql = "INSERT INTO " + getTableName() + " (precio) VALUES (?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, producto.getPrecio());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    producto.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al añadir producto.", e);
        }
    }

    public Producto get(int id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToProducto(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el producto.", e);
        }

        return null;
    }

    public void delete(int id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el producto.", e);
        }
    }
    public Producto getByName(String nombre) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE nombre = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToProducto(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el producto por nombre.", e);
        }

        return null;
    }



    protected abstract String getTableName();
    protected abstract Producto mapResultSetToProducto(ResultSet rs) throws SQLException;
    public abstract List<? extends Producto> getAll();
}
