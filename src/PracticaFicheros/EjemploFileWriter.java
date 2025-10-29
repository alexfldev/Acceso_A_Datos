package PracticaFicheros;

import java.io.FileWriter;
import java.io.IOException;

public class EjemploFileWriter {
    public static void main(String[] args) {
        //Variable para almacenar el caracter leído
        String contenido = "Primera línea\nSegunda línea\nTercera línea";

        //Try-catch cierra automáticamente el FileWriter
        //Por defecto sobrescribe el archivo
        try(FileWriter fw = new FileWriter("C:\\Users\\AlumnoAfternoon\\Documents\\Pruebas-Java\\salida2.txt")) {
            //read() devuelve -1 cuándo llega al final del archivo, por eso lemos enteros
            fw.write(contenido);
            //habría que poner flush() si no se usa FileWriter dentro de un try catch, esto cierra el escritor
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
