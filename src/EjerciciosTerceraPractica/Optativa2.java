package EjerciciosTerceraPractica;

import java.io.*;
import java.sql.*;
import java.util.Properties;
public class Optativa2 {

        /**
         * Migra todas las propiedades de archivo a base de datos.
         * Si la clave ya existe en la BD, la sobreescribe (Upsert).
         */
        public static int migrarPropertiesABD(String archivo, Connection conn) throws IOException, SQLException {
            Properties props = new Properties();
            // Cargar archivo
            try (FileInputStream fis = new FileInputStream(archivo)) {
                props.load(fis);
            }

            System.out.println("Migrando propiedades a BD...");

            // Usamos REPLACE INTO (Sintaxis MySQL) para insertar o reemplazar si ya existe la clave
            String sql = "REPLACE INTO configuracion (clave, valor) VALUES (?, ?)";
            int contador = 0;

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (String clave : props.stringPropertyNames()) {
                    String valor = props.getProperty(clave);

                    pstmt.setString(1, clave);
                    pstmt.setString(2, valor);
                    pstmt.executeUpdate();

                    System.out.println("  " + clave + " = " + valor);
                    contador++;
                }
            }
            return contador;
        }

        /**
         * Exporta configuración de base de datos a archivo Properties.
         */
        public static int exportarBDaProperties(Connection conn, String archivo) throws SQLException, IOException {
            Properties props = new Properties();
            String sql = "SELECT clave, valor FROM configuracion";
            int contador = 0;

            System.out.println("Exportando configuración de BD a archivo...");

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    String clave = rs.getString("clave");
                    String valor = rs.getString("valor");
                    props.setProperty(clave, valor);
                    contador++;
                }
            }

            // Guardar en archivo
            try (FileOutputStream fos = new FileOutputStream(archivo)) {
                props.store(fos, "Configuración exportada desde Base de Datos");
            }

            return contador;
        }

        /**
         * Sincroniza: actualiza la BD solo si el valor en el archivo Properties es diferente
         * al que hay en la base de datos.
         */
        public static int sincronizarPropiedades(String archivo, Connection conn) throws IOException, SQLException {
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream(archivo)) {
                props.load(fis);
            }

            String sqlCheck = "SELECT valor FROM configuracion WHERE clave = ?";
            String sqlUpdate = "UPDATE configuracion SET valor = ? WHERE clave = ?";
            int actualizados = 0;

            try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck);
                 PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate)) {

                for (String clave : props.stringPropertyNames()) {
                    String valorArchivo = props.getProperty(clave);

                    // 1. Verificar valor actual en BD
                    pstmtCheck.setString(1, clave);
                    try (ResultSet rs = pstmtCheck.executeQuery()) {
                        if (rs.next()) {
                            String valorBD = rs.getString("valor");

                            // 2. Si son diferentes, actualizar
                            if (!valorArchivo.equals(valorBD)) {
                                pstmtUpdate.setString(1, valorArchivo);
                                pstmtUpdate.setString(2, clave);
                                pstmtUpdate.executeUpdate();
                                System.out.println("  Sincronizado cambio: " + clave + " -> " + valorArchivo);
                                actualizados++;
                            }
                        } else {
                            // (Opcional) Si no existe en BD, se podría insertar aquí
                        }
                    }
                }
            }
            return actualizados;
        }

        // --- MÉTODOS AUXILIARES ---

        /**
         * Crea la tabla necesaria para el ejercicio
         */
        public static void crearTablaConfiguracion(Connection conn) throws SQLException {
            System.out.println("Creando tabla 'configuracion'...");
            String sql = "CREATE TABLE IF NOT EXISTS configuracion (" +
                    "clave VARCHAR(50) PRIMARY KEY, " +
                    "valor VARCHAR(255))";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                // Limpiamos la tabla para que el ejercicio empiece de cero
                stmt.execute("DELETE FROM configuracion");
            }
        }

        /**
         * Crea un archivo properties dummy para probar la migración
         */
        public static void crearArchivoPrueba(String archivo) throws IOException {
            Properties p = new Properties();
            p.setProperty("db.host", "localhost");
            p.setProperty("db.port", "3306");
            p.setProperty("app.nombre", "Mi App");
            p.setProperty("app.version", "2.0");

            try (FileOutputStream fos = new FileOutputStream(archivo)) {
                p.store(fos, "Archivo inicial");
            }
        }

        // --- MAIN ---
        public static void main(String[] args) {
            String url = "jdbc:mysql://localhost:3306/testdb"; // Usamos tu misma BD 'testdb'
            String user = "root";
            String password = "mysql"; // <--- Poner contraseña si es necesaria

            String archivoOrigen = "config.properties";
            String archivoDestino = "config_exportado.properties";

            try (Connection conn = DriverManager.getConnection(url, user, password)) {

                // 0. Preparativos (Crear tabla y archivo dummy)
                crearTablaConfiguracion(conn);
                crearArchivoPrueba(archivoOrigen);

                // 1. Migrar de Archivo -> BD
                int migradas = migrarPropertiesABD(archivoOrigen, conn);
                System.out.println("Propiedades migradas a BD: " + migradas + "\n");

                // 2. Modificar un dato en la BD manualmente (Simular un cambio externo)
                System.out.println("--- Simulando cambio en BD (db.port -> 3307) ---");
                try (PreparedStatement ps = conn.prepareStatement("UPDATE configuracion SET valor = ? WHERE clave = ?")) {
                    ps.setString(1, "3307");
                    ps.setString(2, "db.port");
                    ps.executeUpdate();
                }

                // 3. Exportar de BD -> Archivo nuevo
                int exportadas = exportarBDaProperties(conn, archivoDestino);
                System.out.println("Propiedades exportadas a archivo: " + exportadas + "\n");

                // 4. Prueba de Sincronización (Archivo original -> BD)
                // El archivo original tiene puerto 3306, la BD tiene 3307. Debería restaurar 3306.
                System.out.println("--- Sincronizando (Restaurando valor original del archivo) ---");
                int sync = sincronizarPropiedades(archivoOrigen, conn);
                System.out.println("Propiedades sincronizadas: " + sync);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


