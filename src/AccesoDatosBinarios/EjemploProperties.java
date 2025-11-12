package AccesoDatosBinarios;


import java.io.*;
import java.util.Properties;

public class EjemploProperties {
    public static void main(String[] args) {
        //inicializamos el objeto properties
        Properties config = new Properties();

        //try catch en el que inicializamos fileinputstream y se cierra automaticamente
        //intentar cargar archivo existente
        try(FileInputStream fis = new FileInputStream("config.properties")){
            //cargamos el archivo
            config.load(fis);
            System.out.println("Configuracion cargada desde el archivo");

        } catch (IOException e) {
            System.out.println("Creando configuracion por defecto");
            config.setProperty("db.host", "localhost");
            config.setProperty("db.port", "3306");
            config.setProperty("db.name", "mysql");
            config.setProperty("db.debug", "false");
        }

        // leer propiedades
        String host = config.getProperty("db.host");
        String port = config.getProperty("db.port");
        String db = config.getProperty("db.name");
        boolean debug = Boolean.parseBoolean(config.getProperty("db.debug"));

        //mostrar configuracion leida

        System.out.println("=== Configuracion ===");
        System.out.println("host: " + host);
        System.out.println("port: " + port);
        System.out.println("db: " + db);
        System.out.println("debug: " + debug);

        //try catch en el que inicializamos FileOutputStream y se cierra automaticamente
        //guardar la configuracion
        try(FileOutputStream fos = new FileOutputStream("config.properties")){
            //cargamos el archivo
            config.store(fos, "Configuracion de la aplicacion");
            System.out.println("\nConfiguracion guardada");

        } catch (IOException e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }

    }
}