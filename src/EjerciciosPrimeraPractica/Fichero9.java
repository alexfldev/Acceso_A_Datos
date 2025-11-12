package EjerciciosPrimeraPractica;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;
import java.net.URI;

/*
Menú principal con opciones:
Verificar archivo
Explorar carpeta
Crear carpeta
Crear archivo
Trabajar con URIs
Salir
Funciones principales: mostrarMenu(), verificarArchivo(), explorarDirectorio(), crearCarpeta(), crearArchivo(), trabajarConURI().
Requisitos: cada función independiente, uso de Scanner, switch, do-while, manejo de errores.
 */
public class Fichero9 {
    public static void main(String[] args) {
        // CORRECCIÓN: Usamos try-with-resources para que el Scanner se cierre solo
        try (Scanner sc = new Scanner(System.in)) {
            mostrarMenu(sc);
        }
    }

    public static void mostrarMenu(Scanner sc) {
        int op = 0;
        do {
            System.out.println("\n*** MENÚ DEL ASISTENTE DE ARCHIVOS ***");
            System.out.println("1. Verificar archivo");
            System.out.println("2. Explorar carpeta");
            System.out.println("3. Crear carpeta");
            System.out.println("4. Crear archivo");
            System.out.println("5. Trabajar con URIs");
            System.out.println("6. Salir");
            System.out.print("Introduce la opción a realizar (1 - 6): ");

            try {
                op = sc.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("Error: Entrada inválida. Por favor, introduce un número.");
                op = 0; // Reseteamos 'op' para que el bucle continúe
            } finally {
                sc.nextLine(); // Limpiamos el buffer del scanner
            }

            System.out.println(); // Salto de línea para limpiar la salida

            switch (op) {
                case 1: verificarArchivo(sc); break;
                case 2: explorarDirectorio(sc); break;
                case 3: crearCarpeta(sc); break;
                case 4: crearArchivo(sc); break;
                case 5: trabajarConURI(sc); break;
                case 6: System.out.println("Saliendo del menú..."); break;
                default:
                    System.err.println("El número introducido no es ninguna de las opciones del menú, prueba de nuevo.");
            }

            if (op != 6) {
                System.out.println("\n--- Presiona Enter para continuar ---");
                sc.nextLine();
            }
        } while(op != 6);
    }

    public static void verificarArchivo(Scanner sc) {
        System.out.print("Introduce la ruta completa al archivo: ");
        String ruta = sc.nextLine();
        File rutaArchivo = new File(ruta);

        if (!rutaArchivo.exists()) {
            System.err.println("Error: La ruta que has pasado no existe.");
        } else if (rutaArchivo.isDirectory()) {
            System.err.println("Error: La ruta que has pasado es un directorio, no un archivo.");
        } else if (rutaArchivo.isFile()) {
            long pesoArchivoEnBytes = rutaArchivo.length();
            System.out.println("Éxito: El archivo existe en la ruta indicada.");
            System.out.println("Peso: " + pesoArchivoEnBytes + " bytes.");
        }
    }

    public static void explorarDirectorio(Scanner sc)  {
        System.out.print("Introduce la ruta del directorio que quieras explorar: ");
        String rutaDirectorio = sc.nextLine();
        File ruta = new File(rutaDirectorio);

        if (!ruta.exists()) {
            System.err.println("Error: La ruta introducida no existe en el equipo.");
        } else if (!ruta.isDirectory()) {
            System.err.println("Error: La ruta introducida es un archivo, no un directorio.");
        } else {
            System.out.println("Contenido de: " + ruta.getAbsolutePath());
            String[] elementos = ruta.list();

            // CORRECCIÓN: Comprobamos si se pudo leer Y si tiene contenido
            if (elementos != null && elementos.length > 0) {
                for (String nombreElemento  : elementos) {
                    File elementoEnArray = new File(ruta, nombreElemento);
                    if (elementoEnArray.isFile()) {
                        long pesoArchivoEnBytes = elementoEnArray.length();
                        System.out.println("[Archivo] " + nombreElemento + " (" + pesoArchivoEnBytes + " bytes)");
                    } else if (elementoEnArray.isDirectory()) {
                        String[] subElementos = elementoEnArray.list();
                        int numElementos = subElementos != null ? subElementos.length : 0;
                        System.out.println("[Directorio] " + nombreElemento + " (" + numElementos + " elementos)");
                    }
                }
            } else if (elementos == null) {
                System.err.println("Error: No se pudo listar el contenido (posiblemente por falta de permisos).");
            } else {
                // elementos.length == 0
                System.out.println("El directorio está vacío.");
            }
        }
    }

    public static void crearCarpeta(Scanner sc) {
        System.out.print("Introduce la ruta de la carpeta que quieres crear: ");
        String rutaCarpeta = sc.nextLine();
        File ruta = new File(rutaCarpeta);

        if (ruta.exists()) {
            System.err.println("Error: La ruta ya existe (sea archivo o carpeta).");
        } else {
            // Usamos mkdirs() para crear carpetas padre si es necesario
            if (ruta.mkdirs()) {
                System.out.println("Éxito: Carpeta(s) creada(s) en: " + ruta.getAbsolutePath());
            } else {
                System.err.println("Error: No se pudo crear la carpeta. Comprueba los permisos.");
            }
        }
    }

    public static void crearArchivo(Scanner sc) {
        System.out.print("Introduce la ruta completa del archivo que quieres crear: ");
        String rutaArchivo = sc.nextLine();
        File ruta = new File(rutaArchivo);

        // CORRECCIÓN: Lógica de creación más segura
        if (ruta.exists()) {
            System.err.println("Error: El archivo (o carpeta) ya existe en esa ruta.");
            return;
        }

        // Aseguramos que el directorio padre exista
        File carpetaPadre = ruta.getParentFile();
        if (carpetaPadre != null && !carpetaPadre.exists()) {
            System.out.println("La ruta padre no existe. Creando directorios...");
            if (carpetaPadre.mkdirs()) {
                System.out.println("Ruta padre creada en: " + carpetaPadre.getAbsolutePath());
            } else {
                System.err.println("Error: No se pudo crear la ruta padre. No se creará el archivo.");
                return; // Salimos si no se puede crear la ruta
            }
        }

        // Ahora que sabemos que la ruta padre existe, creamos el archivo
        try {
            if (ruta.createNewFile()) {
                System.out.println("Éxito: Archivo creado en: " + ruta.getAbsolutePath());
            } else {
                // Esto es raro si ya comprobamos exists(), pero es una seguridad extra
                System.err.println("Error: El archivo no se pudo crear (razón desconocida).");
            }
        } catch (IOException e) {
            // CORRECCIÓN: Manejamos la excepción para que el menú no se rompa
            System.err.println("Error de E/S al crear el archivo: " + e.getMessage());
        }
    }

    public static void trabajarConURI(Scanner sc) {
        System.out.print("Introduce la ruta que quieres pasar a URI: ");
        String rutaUri = sc.nextLine();
        File ruta = new File(rutaUri);

        if (!ruta.exists()) {
            System.err.println("Aviso: La ruta no existe, pero se intentará convertir a URI.");
        }

        try {
            URI uri = ruta.toURI();
            System.out.println("Ruta: " + ruta.getPath());
            System.out.println("URI:  " + uri.toString());
        } catch (Exception e) {
            // CORRECCIÓN: Usamos System.err para errores
            System.err.println("Error al intentar convertir la ruta a URI: " + e.getMessage());
        }
    }
}