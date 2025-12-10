package EjerciciosTerceraPractica;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

    public class SistemaUsuarios2 {


        public static void crearTabla(Connection conn) throws SQLException {
            String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100) NOT NULL, " +
                    "edad INT)";

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("Tabla 'usuarios' creada");
            }
        }

        /**
         * Inserta un nuevo usuario en la base de datos y devuelve su ID
         */
        public static int insertarUsuario(Connection conn, String nombre, String email, int edad) throws SQLException {
            // Validación básica
            if (nombre == null || email == null) {
                throw new IllegalArgumentException("El nombre y el email no pueden ser nulos");
            }

            String sql = "INSERT INTO usuarios (nombre, email, edad) VALUES (?, ?, ?)";

            // RETURN_GENERATED_KEYS es necesario para obtener el ID autonumérico
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, nombre);
                pstmt.setString(2, email);
                pstmt.setInt(3, edad);

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("La creación del usuario falló, no se insertaron filas.");
                }

                // Recuperar el ID generado
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        System.out.println("Usuario insertado con ID: " + id);
                        return id;
                    } else {
                        throw new SQLException("La creación del usuario falló, no se obtuvo ID.");
                    }
                }
            }
        }

        /**
         * Busca usuarios por nombre (búsqueda parcial usando LIKE)
         */
        public static List<Usuario> buscarPorNombre(Connection conn, String nombre) throws SQLException {
            List<Usuario> lista = new ArrayList<>();
            String sql = "SELECT * FROM usuarios WHERE nombre LIKE ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Añadimos % para que busque coincidencias parciales (ej: "Juan" encuentra "Juan Pérez")
                pstmt.setString(1, "%" + nombre + "%");

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Usuario u = new Usuario();
                        u.setId(rs.getInt("id"));
                        u.setNombre(rs.getString("nombre"));
                        u.setEmail(rs.getString("email"));
                        u.setEdad(rs.getInt("edad"));
                        lista.add(u);
                    }
                }
            }
            System.out.println("Usuarios encontrados (búsqueda '" + nombre + "'):");
            return lista;
        }

        /**
         * Actualiza el email de un usuario
         */
        public static boolean actualizarEmail(Connection conn, int id, String nuevoEmail) throws SQLException {
            String sql = "UPDATE usuarios SET email = ? WHERE id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nuevoEmail);
                pstmt.setInt(2, id);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Email actualizado para usuario ID: " + id);
                    return true;
                }
            }
            return false;
        }

        /**
         * Elimina un usuario por ID
         */
        public static boolean eliminarUsuario(Connection conn, int id) throws SQLException {
            String sql = "DELETE FROM usuarios WHERE id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Usuario eliminado: ID " + id);
                    return true;
                }
            }
            return false;
        }

        // --- MAIN: CASOS DE USO ---
        public static void main(String[] args) {
            // Configuración de la conexión (Ajusta usuario/password según tu entorno)
            String url = "jdbc:mysql://localhost:3306/testdb";
            String user = "root";
            String password = "mysql"; // Pon aquí tu contraseña de MySQL

            try (Connection conn = DriverManager.getConnection(url, user, password)) {

                // 1. Crear tabla
                crearTabla(conn);

                // 2. Insertar usuarios

                int id1 = insertarUsuario(conn, "Juan Pérez", "juan@email.com", 25);
                int id2 = insertarUsuario(conn, "María García", "maria@email.com", 30);

                // 3. Buscar usuarios
                List<Usuario> usuarios = buscarPorNombre(conn, "Juan");
                for (Usuario u : usuarios) {
                    System.out.println(u);
                }

                // 4. Actualizar email
                actualizarEmail(conn, id1, "juan.nuevo@email.com");

                // 5. Eliminar usuario
                eliminarUsuario(conn, id2);

            } catch (SQLException e) {
                System.err.println("Error de Base de Datos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // --- CLASE USUARIO ---
    class Usuario {
        private int id;
        private String nombre;
        private String email;
        private int edad;

        public Usuario() {}

        public Usuario(int id, String nombre, String email, int edad) {
            this.id = id;
            this.nombre = nombre;
            this.email = email;
            this.edad = edad;
        }

        // Getters y Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public int getEdad() { return edad; }
        public void setEdad(int edad) { this.edad = edad; }

        @Override
        public String toString() {
            return "ID: " + id + ", Nombre: " + nombre + ", Email: " + email + ", Edad: " + edad;

        }
    }

