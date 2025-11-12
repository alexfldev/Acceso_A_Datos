package EjerciciosSegundaPractica;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
// Importaciones necesarias para Files.move
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 *
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

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructor requerido.
     * @param archivoLog El nombre base del archivo de log.
     * @param tamanoMaximo El tamaño máximo en bytes antes de rotar.
     */
    public SistemaDeLog(String archivoLog, long tamanoMaximo) {
        // Validar parámetros
        if (archivoLog == null || archivoLog.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo no puede ser nulo o vacío.");
        }
        if (tamanoMaximo <= 0) {
            throw new IllegalArgumentException("El tamaño máximo debe ser un valor positivo.");
        }

        this.archivoLog = archivoLog;
        this.tamanoMaximo = tamanoMaximo;
        this.numeroRotacion = 0;
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
        String fechaActualFormateada = LocalDateTime.now().format(FORMATO_FECHA);
        String lineaAEscribir = String.format("[%s] [%s] %s",
                fechaActualFormateada, nivel, mensaje);

        // 3. Escribir en el archivo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.archivoLog, true))) {
            bw.write(lineaAEscribir);
            bw.newLine();
            bw.flush();
        }

        // Requisito del Caso de Uso: Mostrar en consola
        System.out.println("Log escrito: " + mensaje);
    }

    /**
     * Verifica si el archivo debe rotarse y ejecuta la rotación
     * (Versión corregida con Files.move)
     * @return true si se realizó la rotación
     * @throws IOException si hay error en la rotación
     */
    private boolean rotarSiNecesario() throws IOException {
        long tamanoActual = obtenerTamanoLog();

        if (tamanoActual >= this.tamanoMaximo) {
            this.numeroRotacion++;

            // Usar java.nio.file.Path
            java.nio.file.Path archivoOriginalPath = Paths.get(this.archivoLog);
            java.nio.file.Path archivoRenombradoPath = Paths.get(this.archivoLog + "." + this.numeroRotacion);

            try {
                // Usar Files.move() en lugar de renameTo()
                Files.move(archivoOriginalPath, archivoRenombradoPath, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("ROTACIÓN: " + this.archivoLog + " renombrado a " + archivoRenombradoPath.getFileName().toString());
                return true;

            } catch (FileSystemException e) {
                System.err.println("Error: No se pudo rotar el archivo (probablemente sigue en uso): " + e.getMessage());
                this.numeroRotacion--; // Revertir contador
                return false;
            } catch (IOException e) {
                System.err.println("Error de E/S al rotar el archivo log: " + e.getMessage());
                this.numeroRotacion--; // Revertir contador
                return false;
            }
        }

        return false; // No fue necesario rotar
    }

    /**
     * Firma de función requerida.
     * Obtiene el tamaño actual del archivo de log.
     *
     * @return tamaño en bytes
     */
    private long obtenerTamanoLog() {
        File archivo = new File(this.archivoLog);

        if (archivo.exists() && archivo.isFile()) {
            return archivo.length();
        } else {
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