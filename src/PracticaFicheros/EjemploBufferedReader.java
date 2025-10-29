package PracticaFicheros;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class EjemploBufferedReader {
    public static void main(String[] args) {
        //Variable para almacenar la línea
        String linea;
        //Contador de líneas
        int numLinea = 0;

        //Buffered Reader envuelve al objeto FileReader para añadir Buffer, se cierra sólo gracias al try-catch
        try(BufferedReader br = new BufferedReader(new FileReader("src/SegundaPractica/entrada.txt"))) {
            while ((linea = br.readLine()) != null) {
                numLinea++;
                System.out.println(numLinea + ": " + linea);
            }
        } catch(IOException e) {
            System.err.println("Error al cargar el archivo " + e.getMessage());
        }
    }
}
