package EjerciciosTerceraPractica;

import java.sql.*;
import java.io.*; // <--- ESTA ERA LA LÍNEA QUE FALTABA Y CAUSABA LOS FALLOS

public class Optativa1 {

    /**
     * Exporta todos los productos de la base de datos a archivo binario
     * Lee fila por fila de la BD y escribe dato por dato en el archivo.
     */
    public static int exportarProductos(Connection conn, String archivo) throws SQLException, IOException {
        String sql = "SELECT id, nombre, precio, stock FROM productos";
        int contador = 0;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
             DataOutputStream dos = new DataOutputStream(new FileOutputStream(archivo))) {

            System.out.println("--- Iniciando Exportación ---");

            while (rs.next()) {
                // 1. Leer de la Base de Datos
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int stock = rs.getInt("stock");

                // 2. Escribir en Archivo Binario (IMPORTANTE: Mantener este orden)
                dos.writeInt(id);
                dos.writeUTF(nombre);
                dos.writeDouble(precio);
                dos.writeInt(stock);

                System.out.println("Producto exportado: ID=" + id + ", Nombre=" + nombre);
                contador++;
            }
        }
        return contador;
    }

    /**
     * Importa productos desde archivo binario a la base de datos
     * Lee del archivo binario e inserta en la BD.
     */
    public static int importarProductos(Connection conn, String archivo) throws SQLException, IOException {
        String sql = "INSERT INTO productos (id, nombre, precio, stock) VALUES (?, ?, ?, ?)";
        int contador = 0;
        File file = new File(archivo);

        if (!file.exists()) {
            System.out.println("El archivo de backup no existe.");
            return 0;
        }

        System.out.println("--- Iniciando Importación ---");

        // Usamos una transacción para que sea más rápido y seguro
        conn.setAutoCommit(false);

        try (DataInputStream dis = new DataInputStream(new FileInputStream(file));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            while (true) {
                // 1. Leer del Archivo Binario (Mismo orden que escritura)
                // Lanzará EOFException cuando se acaben los datos
                int id = dis.readInt();
                String nombre = dis.readUTF();
                double precio = dis.readDouble();
                int stock = dis.readInt();

                // 2. Preparar Insert SQL
                pstmt.setInt(1, id);
                pstmt.setString(2, nombre);
                pstmt.setDouble(3, precio);
                pstmt.setInt(4, stock);

                pstmt.executeUpdate();

                System.out.println("Producto importado: ID=" + id + ", Nombre=" + nombre);
                contador++;
            }

        } catch (EOFException e) {
            // Fin del archivo alcanzado normalmente
        } catch (SQLException e) {
            // Si falla algo, deshacemos cambios
            conn.rollback();
            throw e;
        } finally {
            // Confirmamos cambios y restauramos el auto-commit
            conn.commit();
            conn.setAutoCommit(true);
        }

        return contador;
    }

    // --- MÉTODOS AUXILIARES PARA PREPARAR LA PRUEBA ---

    public static void prepararEntorno(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // 1. Crear tabla limpia
            stmt.execute("DROP TABLE IF EXISTS productos");
            stmt.execute("CREATE TABLE productos (" +
                    "id INT PRIMARY KEY, " +
                    "nombre VARCHAR(100), " +
                    "precio DOUBLE, " +
                    "stock INT)");

            // 2. Insertar datos iniciales para tener algo que exportar
            stmt.execute("INSERT INTO productos VALUES (1, 'Laptop', 1200.50, 10)");
            stmt.execute("INSERT INTO productos VALUES (2, 'Mouse', 25.00, 50)");
            stmt.execute("INSERT INTO productos VALUES (3, 'Teclado', 45.99, 30)");
            System.out.println("Entorno preparado: Tabla creada y 3 productos insertados.\n");
        }
    }

    public static void limpiarTabla(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM productos");
            System.out.println("\nTabla vaciada (Simulación de pérdida de datos).");
        }
    }

    // --- MAIN ---
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/testdb"; // Asegúrate de que la BD 'testdb' existe
        String user = "root";
        String password = "mysql"; // Tu contraseña aquí

        String archivoBackup = "backup_productos.dat";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            // PASO 0: Preparar datos iniciales en la BD
            prepararEntorno(conn);

            // PASO 1: Exportar (BD -> Archivo)
            int exportados = exportarProductos(conn, archivoBackup);
            System.out.println("Total exportados: " + exportados);

            // PASO 2: Borrar todo (Simular desastre)
            limpiarTabla(conn);

            // PASO 3: Importar (Archivo -> BD)
            int importados = importarProductos(conn, archivoBackup);
            System.out.println("Total importados: " + importados);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}