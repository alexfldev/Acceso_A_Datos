package EjerciciosSegundaPractica;

/*
 * Lee un archivo y cuenta palabras, líneas y caracteres
 * @param nombreArchivo ruta del archivo a analizar
 * @return objeto EstadisticasTexto con los resultados
 * @throws IOException si hay error al leer el archivo
 * Escribe las estadísticas en un archivo de salida
 * @param estadisticas objeto con las estadísticas
 * @param archivoSalida ruta donde guardar el resultado
 * @throws IOException si hay error al escribir
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class EstadisticasTexto {
    private int numeroLineas;
    private int numeroPalabras;
    private int numeroCaracteres;
    private String palabraMasLarga;

    public EstadisticasTexto(int numeroLineas, int numeroPalabras, int numeroCaracteres, String palabraMasLarga) {
        this.numeroLineas = numeroLineas;
        this.numeroPalabras = numeroPalabras;
        this.numeroCaracteres = numeroCaracteres;
        this.palabraMasLarga = palabraMasLarga;
    }

    public int getNumeroLineas() {
        return numeroLineas;
    }
    public void setNumeroLineas(int numeroLineas) {
        this.numeroLineas = numeroLineas;
    }
    public int getNumeroPalabras() {
        return numeroPalabras;
    }
    public void setNumeroPalabras(int numeroPalabras) {
        this.numeroPalabras = numeroPalabras;
    }
    public int getNumeroCaracteres() {
        return numeroCaracteres;
    }
    public void setNumeroCaracteres(int numeroCaracteres) {
        this.numeroCaracteres = numeroCaracteres;
    }
    public String getPalabraMasLarga() {
        return palabraMasLarga;
    }
    public void setPalabraMasLarga(String palabraMasLarga) {
        this.palabraMasLarga = palabraMasLarga;
    }

    public static EstadisticasTexto analizarArchivo(String nombreArchivo) throws IOException{
        List<String> palabras = new ArrayList<>();
        File file = new File(nombreArchivo);
        String linea;
        int numeroLineas = 0;
        int numeroPalabras = 0;
        int numeroCaracteres = 0;
        String palabraMasLarga = "";
        System.out.println("Analizando el texto del archivo: " + file.getAbsolutePath());
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((linea = br.readLine()) != null) {
                //Aumento el contador de líneas
                numeroLineas++;
                //Limpio los espacios del inicio y final con.trim()
                String lineaLimpia = linea.trim();
                //Compruebo si la línea está vacía
                if(!lineaLimpia.isEmpty()) {
                    //Creo un array temporal para sacar las palabras de la línea
                    //Gracias a .split("\\s+") saco palabra a palabra
                    String[] palabrasDeLaLinea = lineaLimpia.split("\\s+");
                    //Vuelco todas las palabras del array temporal a la lista general
                    palabras.addAll(Arrays.asList(palabrasDeLaLinea));
                }
            }

            numeroPalabras = palabras.size();

            for(String palabra: palabras) {
                int longitudPalabra = palabra.length();
                numeroCaracteres += longitudPalabra;
                if(palabraMasLarga.length() < longitudPalabra) {
                    palabraMasLarga = palabra;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Archivo analizado");
        return new EstadisticasTexto(numeroLineas, numeroPalabras, numeroCaracteres, palabraMasLarga);
    }

    public static void guardarEstadisticas(EstadisticasTexto estadisticas, String archivoSalida) {
        File archivoConEStadisticas = new File(archivoSalida);
        System.out.println("Escribiendo estadísticas en: " + archivoConEStadisticas.getAbsolutePath());
        /*
        === Estadísticas del archivo ===
        Líneas: 3
        Palabras: 5
        Caracteres: 35
        Palabra más larga: Programación (13 caracteres)
         */
        String[] lineasAMostrar = {
                "Líneas: " + estadisticas.getNumeroLineas(),
                "Palabras: " + estadisticas.getNumeroPalabras(),
                "Caracteres: " + estadisticas.getNumeroCaracteres(),
                "Palabra más larga: " + estadisticas.getPalabraMasLarga()
        };
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(archivoConEStadisticas))) {
            for(String linea: lineasAMostrar) {
                //escribir línea
                bw.write(linea);
                bw.newLine();
            }
            System.out.println("Estadísticas volcadas en el archivo");
        } catch(IOException e) {
            System.err.println("Error al cargar el archivo " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Escribe el nombre del archivo a analizar: ");
        String nombreArhivoEntrada = sc.nextLine();
        File archivoAnalizable = new File(nombreArhivoEntrada);
        System.out.print("Escribe el nombre del archivo de salida: ");
        String nombreArchivoSalida = sc.nextLine();
        File archivoSalida = new File(nombreArchivoSalida);
        if(archivoAnalizable.isFile() && archivoSalida.isFile() && archivoAnalizable.exists() && archivoSalida.exists()) {
            try {
                guardarEstadisticas(analizarArchivo(nombreArhivoEntrada), nombreArchivoSalida);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.err.println("Error, las rutas no apuntan a archivos o apuntan a archivos que no existen");
        }
        sc.close();
    }

}
