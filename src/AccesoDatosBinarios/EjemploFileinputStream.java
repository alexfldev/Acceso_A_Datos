package AccesoDatosBinarios;
//importamos Librerias
import java.io.FileInputStream;
import java.io.IOException;
public class EjemploFileinputStream {
    public static void main(String[] args) {

        //inicializacion de variable para recorrer fichero .bin
        int b;
        // Try-Catch en el que inicializamos FileInputStream y se cierra automaticamente
        try(FileInputStream fis = new FileInputStream("datos.bin")) {
            //bucle en el que leemos caracter a caracter
            while((b = fis.read()) != -1) {
                System.out.print(b + " ");
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }
}
