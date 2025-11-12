package EjerciciosSegundaPractica;

import java.io.*;
import java.util.Scanner;

public class MergeArchivosFiltrado {

    /**
     * Combina múltiples archivos en uno solo, filtrando líneas
     * @param archivosEntrada array con las rutas de los archivos a combinar
     * @param archivoSalida ruta del archivo resultado
     * @param filtro palabra que debe contener la línea para incluirse (null = todas)
     * @return número total de líneas escritas
     * @throws IOException si hay error de lectura/escritura
     */
    public static int combinarArchivos(String[] archivosEntrada, String archivoSalida, String filtro) throws IOException {
        int totalLineasEscritas = 0;

        // Abrimos el archivo de salida una sola vez (try-with-resources)
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoSalida))) {

            // Iteramos sobre todos los archivos de entrada
            for (String nombreArchivo : archivosEntrada) {
                File archivo = new File(nombreArchivo);
                if (!archivo.exists() || !archivo.isFile()) {
                    System.err.println("Error: El archivo de entrada no existe, se omite: " + nombreArchivo);
                    continue; // Salta al siguiente archivo
                }

                int lineasCoincidentes = 0;
                // Abrimos cada archivo de entrada (try-with-resources anidado)
                try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        // Si cumple el filtro, la escribimos
                        if (cumpleFiltro(linea, filtro)) {
                            bw.write(linea);
                            bw.newLine();
                            lineasCoincidentes++;
                        }
                    }
                }

                // Imprimimos el resultado por archivo, como pide el Caso de Uso
                System.out.println("Procesando " + nombreArchivo + ": " + lineasCoincidentes + " líneas coinciden");
                totalLineasEscritas += lineasCoincidentes;
            }
        }
        // Las excepciones IOException se lanzan automáticamente hacia el main

        return totalLineasEscritas;
    }

    /**
     * Verifica si una línea cumple el criterio de filtrado
     * @param linea línea a evaluar
     * @param filtro criterio de búsqueda (null = siempre true)
     * @return true si la línea debe incluirse
     */
    private static boolean cumpleFiltro(String linea, String filtro) {
        // Si el filtro es nulo o vacío, todas las líneas pasan
        if (filtro == null || filtro.trim().isEmpty()) {
            return true;
        }
        // Devuelve true si la línea contiene el texto del filtro
        return linea.contains(filtro);
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("*** MERGEADOR DE ARCHIVOS ***");
            System.out.print("Escribe el nombre del primer archivo: ");
            String primerArchivo = sc.nextLine();

            System.out.print("Escribe el nombre del segundo archivo: ");
            String segundoArchivo = sc.nextLine();

            // Creamos el array de Strings, como pide el método
            String[] archivosEntrada = {primerArchivo, segundoArchivo};

            System.out.print("Escribe el nombre del archivo de salida: ");
            String nombreArchivoSalida = sc.nextLine();

            // Validación corregida: solo comprobamos los archivos de entrada
            File file1 = new File(primerArchivo);
            File file2 = new File(segundoArchivo);

            if (file1.exists() && file1.isFile() && file2.exists() && file2.isFile()) {
                System.out.println("Rutas de archivos correctas, comenzando el MERGE");
                System.out.print("Escribe el filtro (deja en blanco para no filtrar): ");
                String filtro = sc.nextLine();

                try {
                    int total = combinarArchivos(archivosEntrada, nombreArchivoSalida, filtro);
                    // Imprimimos el resumen total, como pide el Caso de Uso
                    System.out.println("Total: " + total + " líneas escritas en " + nombreArchivoSalida);

                } catch (IOException e) {
                    System.err.println("Error fatal durante la lectura/escritura: " + e.getMessage());
                }
            } else {
                System.err.println("Error: Uno o ambos archivos de entrada no existen o no son archivos.");
            }
        }
    }
}