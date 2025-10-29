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
        Scanner sc = new Scanner(System.in);
        mostrarMenu(sc);
    }

    public static void mostrarMenu(Scanner sc) {
        int op = 0;
        do {
            System.out.println("***MENÚ DEL ASISTENTE***");
            System.out.println();
            System.out.println("1. Verificar archivo");
            System.out.println("2. Explorar carpeta");
            System.out.println("3. Crear carpeta");
            System.out.println("4. Crear archivo");
            System.out.println("5. Trabajar con URIs");
            System.out.println("6. Salir");
            System.out.print("Introduce la opción a realizar(1 - 6): ");

            try {
                op = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, introduce un número.");
                sc.nextLine();
                op = 0;
                continue;
            }

            switch (op) {
                case 1: verificarArchivo(sc); break;
                case 2: explorarDirectorio(sc); break;
                case 3: crearCarpeta(sc); break;
                case 4: crearArchivo(sc); break;
                case 5: trabajarConURI(sc); break;
                case 6: System.out.println("Saliendo del menú"); break;
                default: System.out.println("El número introducido no es ninguna de las opciones del menú, prueba de nuevo");
            }
            if (op != 6) {
                System.out.println("Presiona Enter para continuar...");
                sc.nextLine();
            }
        } while(op != 6);
    }

    public static void verificarArchivo(Scanner sc) {
        System.out.print("Introduce la rutra completa al archivo con este incluido: ");
        String ruta = sc.nextLine();
        File rutaArchivo = new File(ruta);
        if (!rutaArchivo.exists()) {
            System.out.println("La ruta que has pasado no existe. inténtalo de nuevo");
        } else if (rutaArchivo.isDirectory()) {
            System.out.println("La ruta que has pasado no es un archivo sino un directorio");
        } else if (rutaArchivo.isFile()) {
            long pesoArchivoEnBytes = rutaArchivo.length();
            System.out.println("El archivo si existe en la ruta indicada, tiene un peso de " + pesoArchivoEnBytes + " bytes");
        }
    }

    public static void explorarDirectorio(Scanner sc)  {
        System.out.print("Introduce la ruta del directorio que quieras explorar: ");
        String rutaDirectorio = sc.nextLine();
        File ruta = new File(rutaDirectorio);
        if (ruta.exists() && ruta.isDirectory()) {
            String[] elementos = ruta.list();
            if (elementos != null) {
                for (String nombreElemento  : elementos) {
                    File elementoEnArray = new File(ruta, nombreElemento);
                    if (elementoEnArray.isFile()) {
                        long pesoArchivoEnBytes = elementoEnArray.length();
                        System.out.println("Archivo: " + nombreElemento + " [Peso del archivo: " + pesoArchivoEnBytes + " bytes]");
                    } else if (elementoEnArray.isDirectory()) {
                        String[] subElementos = elementoEnArray.list();
                        int numElementos = subElementos != null ? subElementos.length : 0;
                        System.out.println("Directorio: " + nombreElemento + " [Número de elementos del directorio: " + numElementos + "]");
                    }
                }
            } else {
                System.out.println("No se pudieron listar los contenidos del directorio (posiblemente por falta de permisos).");
            }
        } else {
            System.out.println("La ruta introducida es errónea o no existe en el equipo o no es un directorio");
        }
    }

    public static void crearCarpeta(Scanner sc) {
        System.out.print("Introduce la ruta de la carpeta que quieres crear: ");
        String rutaCarpeta = sc.nextLine();
        File ruta = new File(rutaCarpeta);
        if (!ruta.exists()) {
            if(ruta.mkdirs()) {
                System.out.println("Ruta creada correctamente: " + ruta.getAbsolutePath());
            } else {
                System.out.println("Error al crear la ruta. Asegúrate de que la carpeta padre existe.");
            }
        } else {
            System.out.println("La ruta ya existe o es un archivo existente");
        }
    }

    public static void crearArchivo(Scanner sc) {
        System.out.print("Introduce la ruta completa del archivo que quieres crear: ");
        String rutaArchivo = sc.nextLine();
        File ruta = new File(rutaArchivo);
        File carpetPadreRuta = ruta.getParentFile();
        if (!carpetPadreRuta.exists()) {
            if(carpetPadreRuta.mkdirs()) {
                System.out.println("La ruta no existía pero se ha creado correctamente: " + carpetPadreRuta.getAbsolutePath());
                System.out.println("Creando el archivo en la nueva ruta");
            } else {
                System.out.println("La ruta no se ha podido crear");
            }
        } else {
            System.out.println("La ruta ya existía, creando el archivo en la ruta");
        }
        try {
            if (ruta.createNewFile()) {
                System.out.println("Archivo creado: " + ruta.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void trabajarConURI(Scanner sc) {
        System.out.print("Introduce la ruta que quieres pasar a URI: ");
        String rutaUri = sc.nextLine();
        File ruta = new File(rutaUri);
        try {
            URI uri = ruta.toURI();
            System.out.println("Ruta convertida a URI correctamente: " + uri.toString());
        } catch (Exception e) {
            System.out.println("Error al intentar convertir la ruta a URI: " + e.getMessage());
        }
    }
}