package EjerciciosPrimeraPractica;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class Fichero3 {
    public static void main(String[] args) {
        //Utilizamos un try-catch porque el objeto URI tiende a dar errores
        try {
            String uriString = "C:///Users///AlumnoAfternoon///Documents///Pruebas-Java";
            URI uri = new URI(uriString);
            File ruta = new File(uriString);
            if (ruta.exists()) {
                if (ruta.isDirectory()) {
                    System.out.println("La ruta presenta un directorio en " +  uri.toString());
                } else if (ruta.isFile()) {
                    System.out.println("La ruta presenta un directorio en " +  uri.toString());
                }
            } else {
                System.out.println("La URI no existe " + uri.toString());
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
