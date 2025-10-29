package PracticaFicheros;

import java.io.FileReader;
import java.io.IOException;

public class EjemploFileReader {
    public static void main(String[] args) {
        //Variable para almacenar el caracter leído
        int caracter;

        //Try-catch cierra automáticamente el FileReader al meterlo dentro de ()
        try(FileReader fr = new FileReader("C:\\Users\\AlumnoAfternoon\\Documents\\Pruebas-Java\\entrada.txt")) {
            //read() devuelve -1 cuándo llega al final del archivo, por eso lemos enteros
            while ((caracter = fr.read()) != -1) {
                //Convertimos el int a char para mostrar el caracter
                System.out.print((char)caracter);
            }
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo " + e.getMessage());
        }
    }
}
