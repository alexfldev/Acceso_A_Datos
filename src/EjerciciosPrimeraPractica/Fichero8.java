package EjerciciosPrimeraPractica;

import java.util.Scanner;
import java.io.File;
import java.net.URI;

/*
Función explorarCarpeta(String ruta): lista contenido.
Función analizarElemento(String ruta): muestra si es archivo (con tamaño) o carpeta (con número de elementos).
Función convertirAURI(String ruta): convierte ruta a URI.
Practica: list(), isFile(), isDirectory(), toURI().
*/
public class Fichero8 {
    public static void main(String[] args) {
        // Usamos try-with-resources para que el Scanner se cierre solo
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("*** EXPLORADOR DE CARPETAS ***");
            explorarRuta(sc); // Cambiamos el nombre para que sea más genérico
        }
    }

    /**
     * Pide una ruta y la explora, sea un archivo o directorio.
     */
    public static void explorarRuta(Scanner sc) {
        System.out.print("Introduce la ruta del directorio o archivo que quieras explorar: ");
        String rutaCarpeta = sc.nextLine();
        File ruta = new File(rutaCarpeta);

        if (!ruta.exists()) {
            System.err.println("Error: La ruta introducida no existe en el equipo.");
            return;
        }

        // CORRECCIÓN: Comprobamos si es un directorio
        if (ruta.isDirectory()) {
            System.out.println("--- Explorando Directorio: " + ruta.getAbsolutePath() + " ---");
            String[] listaNombres = ruta.list();

            if (listaNombres != null && listaNombres.length > 0) {
                for (String nombreElemento : listaNombres) {
                    File elemento = new File(ruta, nombreElemento);
                    analizarElemento(elemento); // Llamada al método simplificado
                }
            } else {
                System.out.println("El directorio está vacío.");
            }

            // CORRECCIÓN: Añadimos el caso de que sea un archivo
        } else if (ruta.isFile()) {
            System.out.println("--- Analizando Archivo: " + ruta.getAbsolutePath() + " ---");
            analizarElemento(ruta); // Analiza solo ese archivo

        } else {
            System.err.println("Error: La ruta no es ni un archivo ni un directorio.");
        }

        // Convertimos a URI al final
        convertirAURI(ruta);
    }

    /**
     * Analiza un elemento (archivo o directorio) y muestra su información.
     * @param elemento El File a analizar.
     */
    public static void analizarElemento(File elemento) {
        if (elemento.isFile()) {
            long pesoArchivoEnBytes = elemento.length();
            System.out.println("[Archivo] " + elemento.getName() + " (Tamaño: " + pesoArchivoEnBytes + " bytes)");

        } else if (elemento.isDirectory()) {
            String[] elementosInternos = elemento.list();
            int numElementos = (elementosInternos != null) ? elementosInternos.length : 0;
            System.out.println("[Directorio] " + elemento.getName() + " (Contiene: " + numElementos + " elementos)");

        }
    }

    /**
     * Muestra la representación URI de una ruta.
     */
    public static void convertirAURI(File ruta) {
        System.out.println("\n--- Conversión a URI ---");
        try {
            URI uri = ruta.toURI();
            System.out.println("Ruta: " + ruta.getPath());
            System.out.println("URI:  " + uri.toString());
        } catch (Exception e) {
            System.err.println("Error al intentar convertir la ruta a URI: " + e.getMessage());
        }
    }
}