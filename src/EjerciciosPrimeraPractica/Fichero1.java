package EjerciciosPrimeraPractica;

import java.io.*;
public class Fichero1 {
    public static void main(String[] args){
      File dirPadre = new File("C:\\Users\\AlumnoAfternoon\\Documents\\Pruebas-Java");
      String nomHijo = "hijo.txt";
      File archivo = new File(dirPadre, nomHijo);
      //Verificar si el archivo existe
        if(archivo.exists()){
            //Si el archivo existe se muestra un mensaje diciéndo que existe y la ruta dónde está
            System.out.println("El archivo existe en la ruta especificada: " + archivo.getAbsolutePath());
        } else {
            //Si el archivo no existe se muestra un mensaje diciendo que no existe y mostrando la ruta especificada
            System.out.println("El archivo no se ha encontrado");
        }
    }

}
