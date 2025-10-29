package PracticaFicheros;

import java.io.*;

public class EjemploBufferedWriter {
    public static void main(String[] args) {
        //Variable para saber en qué línea del archivo estoy, lo necesito para el if del salto de línea
        int contador = 1;
        //Array con líneas a escribir
        String[] lineas = {
                "Encabezado del documento",
                "Primera línea",
                "Segunda línea",
                "Tercera línea",
                "Final del documento"
        };

        //BufferedWriter envuelve al objeto FileWriter
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("src/SegundaPractica/salidaBuffered.txt"))) {
            for(String linea: lineas) {
                //escribir línea
                bw.write(linea);
                //salto de línea, he creado un bucle para que no me meta un salto de línea al final
                if (contador < lineas.length) {
                    bw.newLine();
                }
                contador++;
            }
        } catch(IOException e) {
            System.err.println("Error al cargar el archivo " + e.getMessage());
        }
    }
}
