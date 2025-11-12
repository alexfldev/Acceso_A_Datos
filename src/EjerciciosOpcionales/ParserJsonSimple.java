package EjerciciosOpcionales;


import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Ejercicio Opcional 1: Parser de JSON Simple
 * Lee y escribe archivos JSON básicos (parsing manual).
 */
public class ParserJsonSimple {

    /**
     * Lee un archivo JSON y extrae pares clave-valor simples
     * @param archivoJson ruta del archivo JSON
     * @return Map con las claves y valores parseados
     * @throws IOException si hay error de lectura
     */
    @SuppressWarnings("rawtypes") // Permitimos Map 'raw' para cumplir la firma del ejercicio
    public static Map leerJsonSimple(String archivoJson) throws IOException {
        Map<String, String> datos = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivoJson))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();

                // Ignoramos las llaves o líneas vacías
                if (linea.equals("{") || linea.equals("}") || linea.isEmpty()) {
                    continue;
                }

                // Dividimos la línea por el primer ':'
                String[] partes = linea.split(":", 2);
                if (partes.length == 2) {
                    // Limpiamos la clave (quitamos comillas)
                    String clave = partes[0].trim().replace("\"", "");

                    // Limpiamos el valor (quitamos comillas y la coma final)
                    String valor = partes[1].trim();
                    if (valor.endsWith(",")) {
                        valor = valor.substring(0, valor.length() - 1);
                    }

                    // Quitamos las comillas del valor
                    if (valor.startsWith("\"") && valor.endsWith("\"")) {
                        valor = valor.substring(1, valor.length() - 1);
                    }

                    datos.put(clave, valor);
                }
            }
        }

        System.out.println("JSON leído: " + datos.size() + " propiedades");
        // Devolvemos el Map<String, String> como un Map 'raw'
        return datos;
    }

    /**
     * Escribe un Map como archivo JSON formateado
     * @param datos Map con los datos a escribir
     * @param archivoJson ruta del archivo de salida
     * @throws IOException si hay error de escritura
     */
    @SuppressWarnings("rawtypes") // Permitimos Map 'raw' para cumplir la firma
    public static void escribirJsonSimple(Map datos, String archivoJson) throws IOException {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoJson))) {
            bw.write("{");
            bw.newLine();

            // Usamos un iterador para poder gestionar la coma final
            // CORRECCIÓN: Especificamos el tipo del iterador
            java.util.Iterator iterador = datos.entrySet().iterator();
            while (iterador.hasNext()) {
                // CORRECCIÓN: Hay que hacer un 'cast' (molde) a Map.Entry
                Map.Entry entrada = (Map.Entry) iterador.next();

                String clave = String.valueOf(entrada.getKey());
                String valor = String.valueOf(entrada.getValue());

                // Escribimos la línea formateada
                bw.write(String.format("  \"%s\": \"%s\"", clave, valor));

                // Añadimos una coma solo si hay más elementos
                if (iterador.hasNext()) {
                    bw.write(",");
                }
                bw.newLine();
            }

            bw.write("}");
        }

        System.out.println("JSON escrito: " + datos.size() + " propiedades en " + archivoJson);
    }

    /**
     * Método main para probar el Ejemplo de uso
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Necesario para el ejemplo de uso
    public static void main(String[] args) {
        String archivoEntrada = "config.json";
        String archivoSalida = "config_nuevo.json";

        // 1. Creamos el 'config.json' de ejemplo para que el test funcione
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoEntrada))) {
            bw.write("{\n");
            bw.write("  \"host\": \"localhost\",\n");
            bw.write("  \"puerto\": \"8080\",\n");
            bw.write("  \"debug\": \"true\"\n");
            bw.write("}\n");
            System.out.println("--- Archivo 'config.json' de prueba creado ---");
        } catch (IOException e) {
            System.err.println("No se pudo crear 'config.json' de prueba: " + e.getMessage());
            return;
        }

        // 2. Ejecutar el caso de uso del ejercicio
        try {
            // Código del ejemplo de uso:
            Map config = leerJsonSimple(archivoEntrada);

            System.out.println("Host: " + config.get("host"));

            config.put("version", "1.0");

            escribirJsonSimple(config, archivoSalida);

        } catch (IOException e) {
            System.err.println("Error procesando el JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
}