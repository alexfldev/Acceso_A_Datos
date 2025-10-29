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
        Scanner sc = new Scanner(System.in);
        System.out.println("***EXPLORADOR DE CARPETAS***");
        explorarCarpeta(sc);
        sc.close();
    }

    public static void explorarCarpeta(Scanner sc)  {
        System.out.print("Introduce la ruta del directorio que quieras explorar: ");
        String rutaCarpeta = sc.nextLine();
        File ruta = new File(rutaCarpeta);
        if (ruta.exists()) {
            for (String nombreElemento  : ruta.list()) {
                File elementoEnArray = new File(ruta, nombreElemento);
                analizarElemento(ruta, elementoEnArray, nombreElemento);
            }
            convertirAURI(ruta);
        } else {
            System.out.println("La ruta introducida es errónea o no existe en el equipo");
        }
    }

    public static void analizarElemento(File ruta, File elementoEnArray, String nombreElemento) {
        if (elementoEnArray.isFile()) {
            long pesoArchivoEnBytes = elementoEnArray.length();
            System.out.println("Archivo: " + nombreElemento + " [Peso del archivo: " + pesoArchivoEnBytes + " bytes]");
        } else if (elementoEnArray.isDirectory()) {
            String[] elementos = elementoEnArray.list();
            System.out.println("Directorio: " + nombreElemento + " [Número de elementos del directorio: " + (elementos != null ? elementos.length : 0) + "]");
        }
    }

    public static void convertirAURI(File ruta) {
        System.out.println("Intentanto convertir la ruta '" + ruta + "' a URI");
        try {
            URI uri = ruta.toURI();
            System.out.println("Ruta convertida a URI correctamente: " + uri.toString());
        } catch (Exception e) {
            // Mensaje de error en caso de fallo de conversión
            System.err.println("Error al intentar convertir la ruta a URI: " + e.getMessage());
        }
    }
}
