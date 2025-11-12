
package EjerciciosSegundaPractica;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Enumeración requerida para los niveles de log.
 */
enum NivelLog {
    INFO, WARNING, ERROR
}


public class SistemaDeLog {

    // --- Campos requeridos ---
    private String archivoLog;
    private long tamanoMaximo; // en bytes
    private int numeroRotacion;

    /**
     * Buena práctica: Definir el formateador de fecha como una constante estática
     * para reutilizarlo y no crearlo en cada escritura.
     */
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructor requerido.
     * @param archivoLog El nombre base del archivo de log.
     * @param tamanoMaximo El tamaño máximo en bytes antes de rotar.
     */
    public SistemaDeLog(String archivoLog, long tamanoMaximo) {
        // Buena práctica: Validar parámetros en el constructor
        if (archivoLog == null || archivoLog.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo no puede ser nulo o vacío.");
        }
        if (tamanoMaximo <= 0) {
            throw new IllegalArgumentException("El tamaño máximo debe ser un valor positivo.");
        }

        this.archivoLog = archivoLog;
        this.tamanoMaximo = tamanoMaximo;
        this.numeroRotacion = 0; // Inicializamos el contador de rotaciones
    }

    /**
     * Firma de función requerida.
     * Escribe un mensaje en el log con timestamp.
     *
     * @param mensaje contenido a registrar
     * @param nivel nivel del log (INFO, WARNING, ERROR)
     * @throws IOException si hay error al escribir
     */
    public void escribirLog(String mensaje, NivelLog nivel) throws IOException {
        // 1. Verificar si hay que rotar ANTES de escribir
        rotarSiNecesario();

        // 2. Preparar la línea de log
        // Buena práctica: Formato de fecha ISO 8601
        String fechaActualFormateada = LocalDateTime.now().format(FORMATO_FECHA);
        String lineaAEscribir = String.format("[%s] [%s] %s",
                fechaActualFormateada, nivel, mensaje);

        // 3. Escribir en el archivo
        // Buenas prácticas: Usar try-with-resources y BufferedWriter
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.archivoLog, true))) {
            bw.write(lineaAEscribir);
            bw.newLine();
            bw.flush(); // Buena práctica: flush() después de escritura crítica
        }

        // Requisito del Caso de Uso: Mostrar en consola
        System.out.println("Log escrito: " + mensaje);
    }


    private boolean rotarSiNecesario() throws IOException {
        long tamanoActual = obtenerTamanoLog();

        if (tamanoActual >= this.tamanoMaximo) {
            // Se superó el tamaño, hay que rotar
            this.numeroRotacion++; // Incrementamos el contador

            File archivoOriginal = new File(this.archivoLog);
            File archivoRenombrado = new File(this.archivoLog + "." + this.numeroRotacion);

            // Intentamos renombrar el archivo actual
            if (archivoOriginal.renameTo(archivoRenombrado)) {
                // Requisito del Caso de Uso: Mostrar en consola
                System.out.println("ROTACIÓN: " + this.archivoLog + " renombrado a " + archivoRenombrado.getName());
                return true;
            } else {
                // Si el renombrado falla
                System.err.println("Error: No se pudo rotar el archivo log: " + this.archivoLog);
                // Revertimos el contador si no se pudo renombrar
                this.numeroRotacion--;
                return false;
            }
        }

        // No fue necesario rotar
        return false;
    }

    /**
     * Firma de función requerida.
     * Obtiene el tamaño actual del archivo de log.
     *
     * @return tamaño en bytes
     */
    private long obtenerTamanoLog() {
        File archivo = new File(this.archivoLog);

        // Comprobamos si el archivo existe y es un archivo
        if (archivo.exists() && archivo.isFile()) {
            return archivo.length();
        } else {
            // Si no existe, su tamaño es 0
            return 0;
        }
    }

    /**
     * Método main para probar funcionamiento según los Casos de Uso.
     */
    public static void main(String[] args) {
        try {
            // Usamos 100 bytes para probar la rotación
            SistemaDeLog log = new SistemaDeLog("app.log", 100);

            log.escribirLog("Aplicación iniciada", NivelLog.INFO);
            log.escribirLog("Usuario 'admin' conectado", NivelLog.INFO);

            // Este log causa la rotación
            log.escribirLog("Error de conexión con la base de datos", NivelLog.ERROR);

            // Este log debera ir a un nuevo archivo 'app.log'
            log.escribirLog("Intento de acceso fallido 'user'", NivelLog.WARNING);
            log.escribirLog("Servicio de pagos contactado", NivelLog.INFO);

        } catch (IOException e) {
            System.err.println("Error fatal de E/S escribiendo el log: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Error de configuración inicial: " + e.getMessage());
        }
    }
}