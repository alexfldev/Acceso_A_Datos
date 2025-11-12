package EjerciciosSegundaPractica;

import java.io.*;
import java.nio.file.Paths; // Importamos 'Paths'
import java.util.Scanner;

public class EstadisticasTexto {
    // Campos 'final' para un objeto inmutable (mejor práctica)
    private final int numeroLineas;
    private final int numeroPalabras;
    private final int numeroCaracteres;
    private final String palabraMasLarga;

    public EstadisticasTexto(int numeroLineas, int numeroPalabras, int numeroCaracteres, String palabraMasLarga) {
        this.numeroLineas = numeroLineas;
        this.numeroPalabras = numeroPalabras;
        this.numeroCaracteres = numeroCaracteres;
        this.palabraMasLarga = palabraMasLarga;
    }

    // --- Getters ---
    public int getNumeroLineas() {
        return numeroLineas;
    }
    public int getNumeroPalabras() {
        return numeroPalabras;
    }
    public int getNumeroCaracteres() {
        return numeroCaracteres;
    }
    public String getPalabraMasLarga() {
        return palabraMasLarga;
    }

    /**
     * Lee un archivo y cuenta palabras, líneas y caracteres.
     * @param nombreArchivo ruta del archivo a analizar
     * @return objeto EstadisticasTexto con los resultados
     * @throws IOException si hay error al leer el archivo
     */
    public static EstadisticasTexto analizarArchivo(String nombreArchivo) throws IOException {
        int numeroLineas = 0;
        int numeroPalabras = 0;
        int numeroCaracteres = 0;
        String palabraMasLarga = "";

        // Esta línea es útil para ver qué ruta completa está intentando usar
        System.out.println("Analizando el texto del archivo: " + new File(nombreArchivo).getAbsolutePath());

        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                numeroLineas++;
                numeroCaracteres += linea.length();
                String lineaLimpia = linea.trim();

                if (!lineaLimpia.isEmpty()) {
                    String[] palabrasDeLaLinea = lineaLimpia.split("\\s+");
                    numeroPalabras += palabrasDeLaLinea.length;

                    for (String palabra : palabrasDeLaLinea) {
                        if (palabra.length() > palabraMasLarga.length()) {
                            palabraMasLarga = palabra;
                        }
                    }
                }
            }
        }

        System.out.println("Archivo analizado.");
        return new EstadisticasTexto(numeroLineas, numeroPalabras, numeroCaracteres, palabraMasLarga);
    }

    /**
     * Escribe las estadísticas en un archivo de salida.
     * @param estadisticas objeto con las estadísticas
     * @param archivoSalida ruta donde guardar el resultado
     * @throws IOException si hay error al escribir
     */
    public static void guardarEstadisticas(EstadisticasTexto estadisticas, String archivoSalida) throws IOException {
        File archivoConEstadisticas = new File(archivoSalida);
        System.out.println("Escribiendo estadísticas en: " + archivoConEstadisticas.getAbsolutePath());

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoConEstadisticas))) {
            bw.write("=== Estadísticas del archivo ===");
            bw.newLine();
            bw.write("Líneas: " + estadisticas.getNumeroLineas());
            bw.newLine();
            bw.write("Palabras: " + estadisticas.getNumeroPalabras());
            bw.newLine();
            bw.write("Caracteres (sin saltos de línea): " + estadisticas.getNumeroCaracteres());
            bw.newLine();
            bw.write("Palabra más larga: " + estadisticas.getPalabraMasLarga());
            bw.newLine();
        }

        System.out.println("Estadísticas guardadas en el archivo.");
    }

    public static void main(String[] args) {

        // --- LÍNEA AÑADIDA PARA DEPURAR ---
        // Esto te dirá DÓNDE está buscando el programa los archivos
        System.out.println("Directorio de trabajo actual: " + Paths.get(".").toAbsolutePath().normalize());

        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Escribe el nombre del archivo a analizar: ");
            String nombreArchivoEntrada = sc.nextLine();

            File archivoEntrada = new File(nombreArchivoEntrada);

            if (!archivoEntrada.exists() || !archivoEntrada.isFile() || !archivoEntrada.canRead()) {
                System.err.println("Error: El archivo de entrada no existe, no es un archivo o no se puede leer.");
                System.err.println("Buscando en: " + archivoEntrada.getAbsolutePath()); // Más info del error
                return;
            }

            System.out.print("Escribe el nombre del archivo de salida: ");
            String nombreArchivoSalida = sc.nextLine();

            try {
                EstadisticasTexto resultados = analizarArchivo(nombreArchivoEntrada);
                guardarEstadisticas(resultados, nombreArchivoSalida);
                System.out.println("Proceso completado exitosamente.");

            } catch (IOException e) {
                System.err.println("Error durante la lectura/escritura del archivo: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}