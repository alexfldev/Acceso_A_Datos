package EjerciciosPrimeraPractica;

import java.io.IOException;
import java.util.Scanner;
import java.io.File;

/*
Función organizarBiblioteca(): crea carpeta de categoría y archivo catalogo.txt.
Función verificarLibro(): verifica si existe un libro; si no, pregunta si se crea.
Practica: exists(), mkdir(), createNewFile(), funciones separadas.
 */
public class Fichero7 {

    // CAMBIO: Usamos una ruta relativa. Esto creará la carpeta "PracticaFicheros"
    // en el directorio donde ejecutes el programa, lo cual es más portable.
    public static final File RUTA = new File("./PracticaFicheros");

    public static void main(String[] args) throws IOException {
        // Usamos try-with-resources para asegurar que el Scanner se cierre
        try (Scanner sc = new Scanner(System.in)) {
            // Creamos la RUTA base si no existe
            if (!RUTA.exists()) {
                if (RUTA.mkdir()) {
                    System.out.println("Directorio base creado en: " + RUTA.getAbsolutePath());
                } else {
                    System.out.println("Error al crear directorio base. Comprueba los permisos.");
                    return; // Salimos si no se puede crear la carpeta base
                }
            }

            if (RUTA.isDirectory()) {
                System.out.println("*** ORGANIZADOR DE BIBLIOTECA ***");
                System.out.println("Usando directorio base: " + RUTA.getAbsolutePath());
                organizarBilbioteca(RUTA, sc);
            } else {
                System.out.println("La RUTA existe pero no es un directorio");
            }
        }
    }

    public static void organizarBilbioteca(File RUTA, Scanner sc) throws IOException {
        System.out.print("Indica la categoría de libros: ");
        String categoria = sc.nextLine();
        System.out.print("Introduce el nombre del libro (con extensión, ej: 'ElQuijote.txt'): ");
        String nombreLibro = sc.nextLine();

        File rutaCategoria = new File(RUTA, categoria);
        // CORRECCIÓN: Arreglado el error de tipeo "Catalago"
        File archivoCatalogo = new File(rutaCategoria, "catalogo.txt");

        if (!rutaCategoria.exists()) {
            System.out.println("El directorio de categoría no existe.");
            if (rutaCategoria.mkdir()) {
                System.out.println("Directorio creado correctamente: " + rutaCategoria.getAbsolutePath());
            } else {
                System.out.println("El directorio no se ha podido crear.");
                return; // Salimos si no se puede crear la carpeta de categoría
            }
        } else {
            System.out.println("El directorio de categoría ya existe: " + rutaCategoria.getAbsolutePath());
        }

        if (!archivoCatalogo.exists()) {
            System.out.println("El archivo catalogo.txt no existe.");
            if (archivoCatalogo.createNewFile()) {
                System.out.println("Archivo catalogo.txt creado correctmente: " + archivoCatalogo.getAbsolutePath());
            } else {
                System.out.println("El archivo catálogo.txt no se ha podido crear.");
            }
        } else {
            System.out.println("El archivo catalogo.txt ya existe: " + archivoCatalogo.getAbsolutePath());
        }

        verificarLibro(rutaCategoria, nombreLibro, sc);
    }

    public static void verificarLibro(File rutaCategoria, String nombreLibro, Scanner sc) throws IOException {
        File archivoLibro = new File(rutaCategoria, nombreLibro);
        String op;
        if (!archivoLibro.exists()) {
            do {
                // CORRECCIÓN: Arreglado el mensaje
                System.out.print("El archivo '" + nombreLibro + "' no existe, ¿quiere crearlo? (si/no) ");
                op = sc.nextLine();

                // CORRECCIÓN: Usamos equalsIgnoreCase para aceptar "si", "Si", "SI"
                if (op.equalsIgnoreCase("si")) {
                    if (archivoLibro.createNewFile()) {
                        System.out.println("Libro añadido: " + archivoLibro.getAbsolutePath());
                    } else {
                        System.out.println("El libro no se ha podido crear");
                    }
                } else if (op.equalsIgnoreCase("no")) {
                    System.out.println("Entendido, no se añadirá el libro");
                } else {
                    System.out.println("Por favor, responde 'si' o 'no'");
                }

                // CORRECCIÓN: El bucle debe continuar mientras la respuesta NO sea "si" Y NO sea "no"
            } while (!op.equalsIgnoreCase("si") && !op.equalsIgnoreCase("no"));
        } else {
            System.out.println("El archivo ya existe: " + archivoLibro.getAbsolutePath());
        }
    }
}