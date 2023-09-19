import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArbolDAO extends ProductoDAO {

    public ArbolDAO(Connection conn) {
        super(conn);
    }


    @Override
    protected String getTableName() {
        return "arboles";
    }

    @Override
    protected Arbol mapResultSetToProducto(ResultSet rs) throws SQLException {
        String nombre = rs.getString("nombre");
        double altura = rs.getDouble("altura");
        double precio = rs.getDouble("precio");
        Arbol arbol = new Arbol(nombre, altura, precio);
        arbol.setId(rs.getInt("id"));
        return arbol;
    }

    @Override
    public Producto get(int id) {
        return super.get(id);
    }

    @Override
    public List<Arbol> getAll() {
        List<Arbol> arboles = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                arboles.add(mapResultSetToProducto(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la lista de árboles.", e);
        }
        return arboles;
    }
    @Override
    public void add(Producto producto) {
        if (!(producto instanceof Arbol)) {
            throw new IllegalArgumentException("Se esperaba un objeto de tipo Arbol");
        }

        Arbol arbol = (Arbol) producto;
        String sql = "INSERT INTO " + getTableName() + " (nombre, altura, precio) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, arbol.getNombre());
            stmt.setDouble(2, arbol.getAltura());
            stmt.setDouble(3, arbol.getPrecio());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    arbol.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al añadir árbol.", e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el árbol.", e);
        }
    }

}
