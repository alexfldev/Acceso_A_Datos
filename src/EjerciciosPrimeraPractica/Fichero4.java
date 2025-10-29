package EjerciciosPrimeraPractica;

import java.io.File;

public class Fichero4 {
    public static void main(String[] args) {
        //Ruta del directorio cómo cadena de texto
        String dirPadre = "C:\\Users\\AlumnoAfternoon\\Documents\\Pruebas-Java";
        //Creación de la instancia usando File
        File directorio = new File(dirPadre);

        //Verificar si el archivo existe y si es un directorio
        if (directorio.exists() && directorio.isDirectory()) {
            //Creaión de un array del contenido de la carpeta de uno en uno
            String[] contenido = directorio.list();

            for (int i = 0; i < contenido.length; i++) {
                System.out.println(contenido[i]);
            }
        } else {
            System.out.println("Error, la siguiente ruta no es un directorio o no existe");
        }
    }
}
