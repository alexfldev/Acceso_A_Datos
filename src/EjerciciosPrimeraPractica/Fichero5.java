package EjerciciosPrimeraPractica;

import java.io.File;
import java.io.IOException;

public class Fichero5 {
    public static void main(String[] args) throws IOException{
        //Rutas del archivo y directorio cómo cadena de texto
        String directorio = "C:\\Users\\AlumnoAfternoon\\Documents\\Pruebas-Java\\Padre";
        String archivo = "C:\\Users\\AlumnoAfternoon\\Documents\\Pruebas-Java\\hijo.txt";

        File directorioPadre = new File(directorio);
        File archivoHijo = new File(archivo);
        boolean fin = false;

        do {
            if(!directorioPadre.exists()) {
                System.out.println("El directorio no existe");
                directorioPadre.mkdir();
                System.out.println("Directorio creado correctamente");
            } else if(!archivoHijo.exists()) {
                System.out.println("El archivo no existe");
                archivoHijo.createNewFile();
                System.out.println("Archivo creado correctamente");
            } else {
                System.out.println("");
                fin = true;
                System.out.println("El directorio y archivo ya existen");
            }
        } while(!fin);
    }
}
