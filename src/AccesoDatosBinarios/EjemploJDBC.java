package AccesoDatosBinarios;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EjemploJDBC {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mysql";
        String usuario = "root";
        String password = "mysql";

        Connection conexion = null;
        Statement sentencia = null;
        ResultSet resultados = null;

        try {
            // Establecer la conexión
            conexion = DriverManager.getConnection(url, usuario, password);
            System.out.println("Conexion establecida");

            // Crear objeto Statement para ejecutar consultas
            sentencia = conexion.createStatement();

            // Ejecutar consulta SELECT
            String sql = "SELECT id, nombre, email, edad FROM usuarios";
            resultados = sentencia.executeQuery(sql);

            // Recorrer los resultados y mostrar por consola
            while (resultados.next()) {
                int id = resultados.getInt("id");
                String nombre = resultados.getString("nombre");
                String email = resultados.getString("email");
                int edad = resultados.getInt("edad");
                System.out.println("ID: " + id + ", Nombre: " + nombre + ", Email: " + email +  ", Edad: " + edad);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            // Cerrar ResultSet, Statement y Connection en orden inverso
            try {
                if (resultados != null) resultados.close();
                if (sentencia != null) sentencia.close();
                if (conexion != null) conexion.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}