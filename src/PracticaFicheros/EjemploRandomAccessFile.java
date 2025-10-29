package PracticaFicheros;

import java.io.RandomAccessFile;
import java.io.IOException;

public class EjemploRandomAccessFile {
    public static void main(String[] args) {
        try(RandomAccessFile  raf = new RandomAccessFile("src\\SegundaPractica\\datos.bin", "rw")) {
            //Vamos a escribir en diferentes posiciones
            raf.writeBytes("INICIO");

            //Nos movemos a la posición 20
            raf.seek(20);
            raf.writeBytes("MEDIO");

            //Nos movemos a las posición 40
            raf.seek(40);
            raf.writeBytes("FINAL");

            //Volver al inicio para leer
            raf.seek(0);
            System.out.println("Posición 0: " + raf.readLine());

            //Volver al inicio para leer
            raf.seek(20);
            System.out.println("Posición 20: " + raf.readLine());

            //Volver al inicio para leer
            raf.seek(40);
            System.out.println("Posición 40: " + raf.readLine());

            //Mostramos la longitud del archivo
            System.out.println("Tamaño del archivo: " + raf.length() + " bytes");
        } catch(IOException e) {
            System.err.println("Error al acceder al archivo: " + e.getMessage());
        }
    }
}
