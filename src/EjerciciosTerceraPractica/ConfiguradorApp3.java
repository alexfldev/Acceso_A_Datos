package EjerciciosTerceraPractica;
import java.io.*;
import java.util.*;



    public class ConfiguradorApp3 {

        /**
         * Carga la configuración desde archivo o crea una por defecto con valores iniciales
         * @param archivo ruta del archivo de configuración
         * @return objeto Properties cargado
         * @throws IOException si hay error de lectura
         */
        public static Properties cargarConfiguracion(String archivo) throws IOException {
            Properties props = new Properties();
            File file = new File(archivo);

            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    props.load(fis);
                    System.out.println("Configuración cargada: " + archivo);
                }
            } else {
                System.out.println("El archivo no existe. Se usarán valores por defecto.");
                // Establecer valores por defecto iniciales (Base de Datos)
                props.setProperty("db.host", "localhost");
                props.setProperty("db.port", "3306");
                props.setProperty("db.name", "mi_base_datos");
                props.setProperty("db.user", "admin");
                props.setProperty("db.password", "secret");

                // Configuración de aplicación
                props.setProperty("app.titulo", "Mi Aplicación");
                props.setProperty("app.version", "1.0.0");
                props.setProperty("app.debug", "false");
                props.setProperty("app.idioma", "en");

                // Configuración de interfaz
                props.setProperty("ui.tema", "claro");
                props.setProperty("ui.tamano_fuente", "12");
            }
            return props;
        }

        /**
         * Obtiene una propiedad como String con valor por defecto
         */
        public static String getString(Properties props, String clave, String valorDefecto) {
            return props.getProperty(clave, valorDefecto);
        }

        /**
         * Obtiene una propiedad como int con validación
         * Captura NumberFormatException si el valor no es numérico
         */
        public static int getInt(Properties props, String clave, int valorDefecto) {
            String valorStr = props.getProperty(clave);
            if (valorStr == null) {
                return valorDefecto;
            }
            try {
                return Integer.parseInt(valorStr);
            } catch (NumberFormatException e) {
                System.err.println("Advertencia: El valor de '" + clave + "' no es un número válido. Usando defecto: " + valorDefecto);
                return valorDefecto;
            }
        }

        /**
         * Obtiene una propiedad como boolean
         */
        public static boolean getBoolean(Properties props, String clave, boolean valorDefecto) {
            String valorStr = props.getProperty(clave);
            if (valorStr == null) {
                return valorDefecto;
            }
            return Boolean.parseBoolean(valorStr);
        }

        /**
         * Guarda la configuración en archivo
         */
        public static void guardarConfiguracion(Properties props, String archivo, String comentario) throws IOException {
            try (FileOutputStream fos = new FileOutputStream(archivo)) {
                props.store(fos, comentario);
                System.out.println("Configuración guardada: " + archivo);
            }
        }

        /**
         * Muestra todas las propiedades por consola
         * Se ordenan alfabéticamente para facilitar la lectura
         */
        public static void mostrarConfiguracion(Properties props) {
            // Usamos un TreeSet para ordenar las claves alfabéticamente al imprimir
            Set<String> clavesOrdenadas = new TreeSet<>(props.stringPropertyNames());

            for (String key : clavesOrdenadas) {
                System.out.println(key + " = " + props.getProperty(key));
            }
        }

        // --- MAIN: CASOS DE USO ---
        public static void main(String[] args) {
            String nombreArchivo = "app.properties";

            try {
                // 1. Cargar configuración (o crear defaults si no existe)
                Properties config = cargarConfiguracion(nombreArchivo);

                // 2. Leer configuración usando los métodos auxiliares
                String dbHost = getString(config, "db.host", "localhost");
                int dbPort = getInt(config, "db.port", 3306);
                boolean debug = getBoolean(config, "app.debug", false);

                System.out.println("=== Configuración Actual ===");
                mostrarConfiguracion(config);

                // 3. Modificar configuración
                System.out.println("\n--- Modificando valores... ---");
                config.setProperty("app.idioma", "es");
                config.setProperty("ui.tema", "oscuro");
                // Cambiamos el puerto para probar la persistencia
                config.setProperty("db.port", "3307");

                // 4. Guardar configuración
                guardarConfiguracion(config, nombreArchivo, "Configuración de Mi Aplicación");

                // Verificación visual de lo que se guardó
                System.out.println("\nArchivo generado (" + nombreArchivo + "):");
                // Leemos el archivo físico solo para mostrarlo por pantalla como pide el ejercicio
                try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        System.out.println(linea); // Muestra el contenido real del archivo con comentarios y fecha
                    }
                }

            } catch (IOException e) {
                System.err.println("Error de E/S: " + e.getMessage());
            }
        }
    }

