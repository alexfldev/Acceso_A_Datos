package EjerciciosOpcionales;



import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Ejercicio Opcional 3: Backup Incremental
 * Copia solo archivos modificados desde el último backup.
 */
public class BackupIncremental {

    // Formateador de fecha para los mensajes de consola
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Realiza backup incremental de una carpeta.
     *
     * @param carpetaOrigen  ruta de la carpeta a respaldar
     * @param carpetaDestino ruta donde guardar el backup
     * @param archivoControl archivo que registra el último backup
     * @return número de archivos copiados
     * @throws IOException si hay error en el proceso
     */
    public static int backupIncremental(String carpetaOrigen, String carpetaDestino, String archivoControl) throws IOException {
        System.out.println("Iniciando backup incremental...");

        // 1. Obtener la hora de inicio de este backup.
        // Solo los archivos modificados *después* del último backup
        // y *antes* de este momento serán copiados.
        long horaInicioBackup = System.currentTimeMillis();

        // 2. Leer la fecha del último backup
        long ultimoBackup = leerUltimoBackup(archivoControl);
        System.out.println("Último backup: " + formatearTimestamp(ultimoBackup));

        // 3. Asegurar que las carpetas existan
        File origen = new File(carpetaOrigen);
        File destino = new File(carpetaDestino);

        if (!origen.exists() || !origen.isDirectory()) {
            throw new FileNotFoundException("La carpeta de origen no existe: " + carpetaOrigen);
        }

        // Crear carpeta de destino si no existe
        if (!destino.exists()) {
            if (destino.mkdirs()) {
                System.out.println("Creada carpeta de destino: " + carpetaDestino);
            }
        }

        // 4. Recorrer archivos y copiar si son nuevos o modificados
        int archivosCopiados = 0;
        File[] archivos = origen.listFiles();

        if (archivos != null) {
            for (File archivoFuente : archivos) {
                // Solo nos interesan los archivos (no subcarpetas)
                if (archivoFuente.isFile()) {
                    // Comprobamos si el archivo ha sido modificado desde el último backup
                    if (archivoFuente.lastModified() > ultimoBackup) {
                        File archivoDestino = new File(destino, archivoFuente.getName());
                        System.out.println("Copiando: " + archivoFuente.getName() + " (modificado)");

                        copiarArchivo(archivoFuente, archivoDestino);
                        archivosCopiados++;
                    }
                }
            }
        }

        // 5. Actualizar el archivo de control con la hora de ESTE backup
        escribirControl(archivoControl, horaInicioBackup);

        return archivosCopiados;
    }

    /**
     * Lee la fecha del último backup desde el archivo de control.
     *
     * @param archivoControl ruta del archivo de control
     * @return timestamp del último backup, o 0 si no existe
     * @throws IOException si hay error de lectura
     */
    private static long leerUltimoBackup(String archivoControl) throws IOException {
        File file = new File(archivoControl);
        if (!file.exists()) {
            return 0; // 0 significa "nunca"
        }

        // Leemos el 'long' (timestamp) que guardamos
        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
            return dis.readLong();
        } catch (EOFException e) {
            // El archivo existe pero está vacío
            return 0;
        }
    }

    /**
     * Escribe el nuevo timestamp en el archivo de control.
     *
     * @param archivoControl ruta del archivo de control
     * @param timestamp      el 'long' (timestamp) a escribir
     * @throws IOException si hay error de escritura
     */
    private static void escribirControl(String archivoControl, long timestamp) throws IOException {
        // Aseguramos que la carpeta del archivo de control exista
        File controlFile = new File(archivoControl);
        if (controlFile.getParentFile() != null) {
            controlFile.getParentFile().mkdirs();
        }

        // Escribimos el 'long' (timestamp)
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(controlFile))) {
            dos.writeLong(timestamp);
        }
        // Salida por consola requerida
        System.out.println("Registro actualizado: " + formatearTimestamp(timestamp));
    }


    /**
     * Copia un archivo de origen a destino usando streams.
     *
     * @param origen  archivo fuente
     * @param destino archivo destino
     * @throws IOException si hay error en la copia
     */
    private static void copiarArchivo(File origen, File destino) throws IOException {
        // Usamos try-with-resources para asegurar que los streams se cierren
        try (InputStream in = new FileInputStream(origen);
             OutputStream out = new FileOutputStream(destino)) {

            byte[] buffer = new byte[4096]; // Buffer de 4KB
            int bytesLeidos;
            while ((bytesLeidos = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesLeidos);
            }
        }
    }

    /**
     * Helper para convertir un timestamp (long) a un String formateado.
     */
    private static String formatearTimestamp(long timestamp) {
        if (timestamp == 0) {
            return "nunca";
        }
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()
        ).format(FORMATO_FECHA);
    }


    /**
     * Método main para probar el Ejemplo de uso.
     */
    public static void main(String[] args) {
        String carpetaOrigen = "./documentos";
        String carpetaDestino = "./backup";
        String archivoControl = "./backup/.lastbackup";

        try {
            // --- PREPARACIÓN: Crear archivos de prueba ---
            new File(carpetaOrigen).mkdirs();
            new File(carpetaDestino).mkdirs();

            System.out.println("--- Creando archivos de prueba ---");
            new FileWriter(new File(carpetaOrigen, "documento1.txt")).close();
            new FileWriter(new File(carpetaOrigen, "documento2.txt")).close();
            new FileWriter(new File(carpetaOrigen, "imagen.png")).close();

            // Pausa para asegurar que los timestamps sean distintos
            Thread.sleep(1000);

            // --- PRIMERA EJECUCIÓN ---
            System.out.println("\n--- PRIMERA EJECUCIÓN (Debe copiar 3 archivos) ---");
            int copiados1 = backupIncremental(carpetaOrigen, carpetaDestino, archivoControl);
            System.out.println("Backup 1 completado: " + copiados1 + " archivos");

            // --- SEGUNDA EJECUCIÓN (Modificando 1 archivo) ---
            System.out.println("\n--- SEGUNDA EJECUCIÓN (Modificando 'documento2.txt') ---");
            // Esperamos 2 segundos para que la fecha de modificación sea claramente posterior
            Thread.sleep(2000);

            // Modificamos un archivo (sobrescribiéndolo)
            try (FileWriter fw = new FileWriter(new File(carpetaOrigen, "documento2.txt"))) {
                fw.write("Contenido modificado");
            }

            int copiados2 = backupIncremental(carpetaOrigen, carpetaDestino, archivoControl);
            System.out.println("Backup 2 completado: " + copiados2 + " archivos (esperado: 1)");

            // --- TERCERA EJECUCIÓN (Sin cambios) ---
            System.out.println("\n--- TERCERA EJECUCIÓN (Sin cambios) ---");
            Thread.sleep(2000); // Esperamos

            int copiados3 = backupIncremental(carpetaOrigen, carpetaDestino, archivoControl);
            System.out.println("Backup 3 completado: " + copiados3 + " archivos (esperado: 0)");

        } catch (IOException e) {
            System.err.println("Error en el proceso de backup: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            // Del Thread.sleep()
            System.err.println("Proceso interrumpido: " + e.getMessage());
        }
    }
}