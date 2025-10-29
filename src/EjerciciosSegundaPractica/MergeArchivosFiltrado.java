package EjerciciosSegundaPractica;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MergeArchivosFiltrado {
    /*
     * Combina múltiples archivos en uno solo, filtrando líneas
     * @param archivosEntrada array con las rutas de los archivos a combinar
     * @param archivoSalida ruta del archivo resultado
     * @param filtro palabra que debe contener la línea para incluirse (null = todas)
     * @return número total de líneas escritas
     * @throws IOException si hay error de lectura/escritura
     */
    public static int combinarArchivos(File[] archivosEntrada, File archivoSalida, String filtro) throws IOException {
        List<String> linesAdded = new ArrayList<>();
        String line;
        try(BufferedReader brFirstFile = new BufferedReader(new FileReader(archivosEntrada[0]))) {
            System.out.println("Procesando el primer archivo: " + archivosEntrada[0].getAbsolutePath());
            while((line = brFirstFile.readLine()) != null) {
                String cleanLine = line.trim();
                if(!cleanLine.isEmpty()) {
                    if(cumpleFiltro(cleanLine, filtro)) {
                        linesAdded.add(cleanLine);
                    }
                }
            }
        } catch(IOException e) {
            System.err.println("Error al cargar el archivo " + e.getMessage());
        }
        int linesSelectedFromFirstFile = linesAdded.size();
        System.out.println("Archivo procesado, líneas añadidas al nuevo archivo: " + linesSelectedFromFirstFile);
        try(BufferedReader brSecondFile = new BufferedReader(new FileReader(archivosEntrada[1]))) {
            System.out.println("Procesando el segundo archivo: " + archivosEntrada[1].getAbsolutePath());
            while((line = brSecondFile.readLine()) != null) {
                String cleanLine = line.trim();
                if(!cleanLine.isEmpty()) {
                    if(cumpleFiltro(cleanLine, filtro)) {
                        linesAdded.add(cleanLine);
                    }
                }
            }
        } catch(IOException e) {
            System.err.println("Error al cargar el archivo " + e.getMessage());
        }
        int linesSelectedFromSecondFile = linesAdded.size() - linesSelectedFromFirstFile;
        System.out.println("Archivo procesado, líneas añadidas al nuevo archivo: " + linesSelectedFromSecondFile);
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(archivoSalida))) {
            for(String linea: linesAdded) {
                bw.write(linea);
                bw.newLine();
            }
            System.out.println("Líneas volcadas al archivo de salida");
        } catch(IOException e) {
            System.err.println("Error al cargar el archivo " + e.getMessage());
        }
        return linesAdded.size();
    }
    /*
     * Verifica si una línea cumple el criterio de filtrado
     * @param linea línea a evaluar
     * @param filtro criterio de búsqueda (null = siempre true)
     * @return true si la línea debe incluirse
     */
    private static boolean cumpleFiltro(String linea, String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            return true;
        }
        String lineaLimpia = linea.trim();
        String[] palabrasDeLaLinea = lineaLimpia.split("\\s+");
        for (String palabra : palabrasDeLaLinea) {
            if (palabra.equals(filtro)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("***MERGEADOR DE ARCHIVOS***");
        System.out.print("Escribe el nombre del primer archivo: ");
        File firstFile = new File(sc.nextLine());
        System.out.print("Escribe el nombre del segundo archivo: ");
        File secondFile = new File(sc.nextLine());
        File[] archivosEntrada = {firstFile, secondFile};
        System.out.print("Escribe el nombre del archivo de salida(el archivo dónde se guardará el MERGE): ");
        File outputFile = new File(sc.nextLine());
        if(firstFile.isFile() && secondFile.isFile() && outputFile.isFile() && firstFile.exists() && secondFile.exists() && outputFile.exists()) {
            System.out.println("Rutas de los archivos correctass, comenzando el MERGE");
            System.out.print("Escribe el filtro que quieres aplicar: ");
            String filter = sc.nextLine();
            try {
                System.out.println("Resultado de merge: " + combinarArchivos(archivosEntrada, outputFile, filter) + " líneas escritas en: " + outputFile.getAbsolutePath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Error, las rutas son incorrectas o no apuntan a archivos");
        }
    }
}
