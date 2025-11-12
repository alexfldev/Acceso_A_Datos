package AccesoDatosBinarios;

// Importación de paquetes
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestMySQLConnection {
    public static void main(String[] args) {

        // Inicialización de variables
        String url = "jdbc:mysql://localhost:3306/mysql";
        String user = "root";
        String password = "mysql";

        // Try-Catch para probar la conexión a la base de datos
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Conexión exitosa a MySQL");
        } catch (SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage());
        }
    }
}
