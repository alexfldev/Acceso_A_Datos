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
    public static final File RUTA = new File("C:\\Users\\AlumnoAfternoon\\Documents\\Pruebas-Java");
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        if (RUTA.exists() && RUTA.isDirectory()) {
            System.out.println("***ORGANIZADOR DE BILBIOTECA***");
            organizarBilbioteca(RUTA, sc);
        } else {
            System.out.println("La RUTA no existe o no es un directorio");
        }
        sc.close();
    }

    public static void organizarBilbioteca(File RUTA, Scanner sc) throws IOException {
        System.out.print("Indica la categoría de libros: ");
        String categoria = sc.nextLine();
        System.out.print("Introduce el nombre del libro: ");
        String nombreLibro = sc.nextLine();
        File rutaCategoria = new File(RUTA, categoria);
        File archivoCatalago = new File(rutaCategoria, "catalogo.txt");
        if (!rutaCategoria.exists()) {
            System.out.println("El directorio no existe");
            if (rutaCategoria.mkdir()) {
                System.out.println("Directorio creado correctamente: " + rutaCategoria.getAbsolutePath());
            } else {
                System.out.println("El directorio no se ha podido crear");
            }
        } else {
            System.out.println("El directorio ya existe: " + rutaCategoria.getAbsolutePath());
        }
        if (!archivoCatalago.exists()) {
            System.out.println("El archivo catalogo.txt no existe");
            if (archivoCatalago.createNewFile()) {
                System.out.println("Archivo catalogo.txt creado correctmente:" + archivoCatalago.getAbsolutePath());
            } else {
                System.out.println("El archivo catálogo.txt no se ha podido crear");
            }
        } else {
            System.out.println("El archivo catalogo.txt ya existe: " + archivoCatalago.getAbsolutePath());
        };
        verificarLibro(rutaCategoria, nombreLibro, sc);
    }

    public static void verificarLibro(File rutaCategoria, String nombreLibro, Scanner sc) throws IOException {
        File archivoLibro = new File(rutaCategoria, nombreLibro);
        String op;
        if (!archivoLibro.exists()) {
            do {
                System.out.print("El archivo + " + nombreLibro +  "no existe, quiere crearlo?(si/no) ");
                op = sc.nextLine();
                if (op.equals("si")) {
                    if (archivoLibro.createNewFile()) {
                        System.out.println("Libro añadido: " + archivoLibro.getAbsolutePath());
                    } else {
                        System.out.println("El libro no se ha podido crear");
                    }
                } else if (op.equals("no")) {
                    System.out.println("Entendido, no se añadirá el libro");
                } else {
                    System.out.println("Por favor, responde si o no");
                }
            } while (!(op.equals("si")));
        } else {
            System.out.println("El archivo ya existe: " + archivoLibro.getAbsolutePath());
        }
    }
}
