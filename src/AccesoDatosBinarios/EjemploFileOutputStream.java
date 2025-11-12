package AccesoDatosBinarios;

import java.io.FileOutputStream;
import java.io.IOException;

public class EjemploFileOutputStream {
    public static void main(String args[]) throws IOException {
        byte[] datos = { 72 , 111, 108 , 97 , 32, 77, 117, 110, 100, 111 ,};
        try (FileOutputStream fos = new FileOutputStream("EjemploFileOutputStream.bin")) {
            //
            fos.write(datos);

        } catch (IOException e) {
            System.out.println("Error al escribir el archivo" + e.getMessage());
        }
    }
}
