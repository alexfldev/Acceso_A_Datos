package EjerciciosPrimeraPractica;

import java.io.File;

public class Fichero2 {
    public static void main(String[] args){
        File ruta = new File("C:\\Users\\AlumnoAfternoon\\Documents\\Pruebas-Java");
        if(ruta.exists()){
            //Verificar si la ruta especificada es un directorio
            if(ruta.isDirectory()){
                //Si en la ruta el último elemento es un directorio, me lo muestra
                System.out.println("La ruta presenta un directorio en: " + ruta.getAbsolutePath());
            } else if(ruta.isFile()){
                //Si en la ruta el último elemento es un archivo, me lo muestra
                System.out.println("La ruta presenta un archivo en: " + ruta.getAbsolutePath());
            }
        }
    }
}
