package EjerciciosOpcionales;



import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Ejercicio Opcional 2: Carga de Variables de Entorno desde .env
 */
public class CargadorEnv {

    // Un Map estático para simular las variables de entorno del sistema
    private static final Map<String, String> variablesCargadas = new HashMap<>();

    /**
     * Lee un archivo .env y carga las variables
     * @param archivoEnv ruta del archivo .env
     * @return Map con las variables cargadas
     * @throws IOException si hay error de lectura
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Para cumplir la firma 'Map'
    public static Map cargarEnv(String archivoEnv) throws IOException {
        // Limpiamos las variables anteriores cada vez que cargamos
        variablesCargadas.clear();

        File archivo = new File(archivoEnv);
        if (!archivo.exists()) {
            throw new FileNotFoundException("El archivo .env no se encuentra en: " + archivo.getAbsolutePath());
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivoEnv))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                // Ignorar comentarios y líneas vacías
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }

                // Dividir por el primer '='
                String[] partes = linea.split("=", 2);

                if (partes.length == 2) {
                    String clave = partes[0].trim();
                    String valor = partes[1].trim();

                    // Quitar comillas opcionales del valor (ej: "valor" o 'valor')
                    if (valor.startsWith("\"") && valor.endsWith("\"")) {
                        valor = valor.substring(1, valor.length() - 1);
                    } else if (valor.startsWith("'") && valor.endsWith("'")) {
                        valor = valor.substring(1, valor.length() - 1);
                    }

                    variablesCargadas.put(clave, valor);
                }
            }
        }

        // Salida por consola requerida
        System.out.println("Cargadas " + variablesCargadas.size() + " variables desde " + archivoEnv);

        // Devolvemos una copia del mapa (y cumplimos la firma 'Map' raw)
        return new HashMap(variablesCargadas);
    }

    /**
     * Obtiene el valor de una variable de entorno "cargada"
     * @param clave nombre de la variable
     * @param valorPorDefecto valor si la variable no existe
     * @return valor de la variable o valorPorDefecto
     */
    public static String getEnv(String clave, String valorPorDefecto) {
        // Usamos getOrDefault del Map estático
        return variablesCargadas.getOrDefault(clave, valorPorDefecto);
    }

    /**
     * Método main para probar el Ejemplo de uso
     */
    public static void main(String[] args) {
        String archivoEnv = ".env";

        // 1. Creamos el archivo .env de ejemplo para que el test funcione
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoEnv))) {
            bw.write("DB_HOST=localhost\n");
            bw.write("DB_PORT=5432\n");
            bw.write("DB_USER=admin\n");
            bw.write("DB_PASSWORD=secret123\n");
            bw.write("\n"); // Línea vacía
            bw.write("# Comentario ignorado\n");
            bw.write("DEBUG=true\n");
            bw.write("APP_NAME='Mi Aplicacion'\n"); // Ejemplo con comillas
            System.out.println("--- Archivo '" + archivoEnv + "' de prueba creado ---");
        } catch (IOException e) {
            System.err.println("No se pudo crear '" + archivoEnv + "' de prueba: " + e.getMessage());
            return;
        }

        // 2. Ejecutar el caso de uso del ejercicio
        try {
            // Código del ejemplo de uso:
            @SuppressWarnings("rawtypes") // Para el 'Map env'
            Map env = cargarEnv(archivoEnv);

            // Salida por consola requerida
            System.out.println("Base de datos: " + env.get("DB_HOST") + ":" + env.get("DB_PORT"));

            // Usamos getEnv (que lee del mapa estático)
            String debug = getEnv("DEBUG", "false");

            // Salida por consola requerida
            System.out.println("Debug mode: " + debug);

            // Prueba extra
            System.out.println("App Name: " + getEnv("APP_NAME", "N/A"));
            System.out.println("API Key: " + getEnv("API_KEY", "NO_API_KEY_SET"));


        } catch (IOException e) {
            System.err.println("Error procesando el archivo .env: " + e.getMessage());
            e.printStackTrace();
        }
    }
}