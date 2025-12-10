package EjerciciosTerceraPractica;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

    public class Optativa3 {

        /**
         * Analiza un archivo binario con la estructura conocida del ejercicio:
         * INT -> UTF -> DOUBLE -> BOOLEAN -> INT
         */
        public static Reporte analizarArchivoBinario(String archivo) throws IOException {
            File file = new File(archivo);
            Reporte reporte = new Reporte(file.getName(), file.length());

            // Necesitamos contar los bytes manualmente para saber la posición
            // DataInputStream no nos dice la posición actual fácilmente
            try (FileInputStream fis = new FileInputStream(file);
                 DataInputStream dis = new DataInputStream(fis)) {

                int bytesLeidos = 0;

                // --- 1. Leer primer INT ---
                int startPos = bytesLeidos;
                int valorInt1 = dis.readInt();
                bytesLeidos += 4; // Un int son 4 bytes
                reporte.agregarElemento(new ElementoDato(startPos, bytesLeidos - 1, "INT", String.valueOf(valorInt1)));
                reporte.incrementarInts();

                // --- 2. Leer String UTF ---
                startPos = bytesLeidos;
                // writeUTF escribe 2 bytes de longitud al principio + los bytes del texto
                // Para calcular bytes leídos necesitamos leer la longitud primero o calcularlo post-lectura
                // Java no expone cuántos bytes leyó readUTF fácilmente, así que usamos un truco:
                // Un UTF en Java es: 2 bytes (length) + N bytes (contenido)
                String valorString = dis.readUTF();
                // Calculamos el tamaño aproximado en bytes (UTF-8 modificado de Java)
                int tamanoStringBytes = 2 + valorString.length();
                bytesLeidos += tamanoStringBytes;
                reporte.agregarElemento(new ElementoDato(startPos, bytesLeidos - 1, "UTF", "\"" + valorString + "\""));
                reporte.incrementarStrings();

                // --- 3. Leer DOUBLE ---
                startPos = bytesLeidos;
                double valorDouble = dis.readDouble();
                bytesLeidos += 8; // Double son 8 bytes
                reporte.agregarElemento(new ElementoDato(startPos, bytesLeidos - 1, "DOUBLE", String.valueOf(valorDouble)));
                reporte.incrementarDoubles();

                // --- 4. Leer BOOLEAN ---
                startPos = bytesLeidos;
                boolean valorBool = dis.readBoolean();
                bytesLeidos += 1; // Boolean es 1 byte
                reporte.agregarElemento(new ElementoDato(startPos, bytesLeidos - 1, "BOOLEAN", String.valueOf(valorBool)));
                // No pediste contador de booleanos en la clase Reporte, pero lo leemos igual

                // --- 5. Leer segundo INT ---
                if (dis.available() > 0) { // Verificamos si queda algo
                    startPos = bytesLeidos;
                    int valorInt2 = dis.readInt();
                    bytesLeidos += 4;
                    reporte.agregarElemento(new ElementoDato(startPos, bytesLeidos - 1, "INT", String.valueOf(valorInt2)));
                    reporte.incrementarInts();
                }
            }

            return reporte;
        }

        /**
         * Muestra el reporte por consola con formato
         */
        public static void mostrarReporte(Reporte reporte) {
            System.out.println("=== Reporte de Análisis de Archivo Binario ===");
            System.out.println("Archivo: " + reporte.getNombreArchivo());
            System.out.println("Tamaño: " + reporte.getTamañoBytes() + " bytes");
            System.out.println("Estructura detectada:");

            for (ElementoDato dato : reporte.getElementos()) {
                System.out.println(dato);
            }

            System.out.println("Resumen:");
            System.out.println(" Enteros (int): " + reporte.getTotalInts());
            System.out.println(" Decimales (double): " + reporte.getTotalDoubles());
            System.out.println(" Cadenas (UTF): " + reporte.getTotalStrings());
            System.out.println(" Total elementos: " + reporte.getElementos().size());
        }

        /**
         * Guarda el reporte en un archivo de texto
         */
        public static void guardarReporte(Reporte reporte, String archivoDestino) throws IOException {
            try (PrintWriter pw = new PrintWriter(new FileWriter(archivoDestino))) {
                pw.println("=== Reporte Generado ===");
                pw.println("Archivo analizado: " + reporte.getNombreArchivo());
                pw.println("Tamaño: " + reporte.getTamañoBytes() + " bytes");
                pw.println("\n--- Detalle de Elementos ---");
                for (ElementoDato dato : reporte.getElementos()) {
                    pw.println(dato.toString());
                }
                pw.println("\n--- Resumen ---");
                pw.println("Total INTs: " + reporte.getTotalInts());
                pw.println("Total DOUBLEs: " + reporte.getTotalDoubles());
                pw.println("Total STRINGs: " + reporte.getTotalStrings());
            }
            System.out.println("Reporte guardado en: " + archivoDestino);
        }

        /**
         * Intenta detectar el tipo de dato (Nota: En binario puro esto es especulativo,
         * aquí lo usamos como helper para obtener el nombre del tipo en texto).
         */
        private static String detectarTipoDato(DataInputStream dis) {
            // En un caso real sin esquema, esto es imposible de saber con certeza.
            // Lo dejamos como método auxiliar requerido por la firma.
            return "Desconocido (Requiere Esquema)";
        }

        // --- MAIN PARA GENERAR Y PROBAR ---
        public static void main(String[] args) {
            String archivoBinario = "datos.dat";
            String archivoReporte = "reporte_datos.txt";

            try {
                // 1. Crear archivo binario (Caso de Uso del ejercicio)
                System.out.println("Generando archivo binario de prueba...");
                try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(archivoBinario))) {
                    dos.writeInt(100);              // 4 bytes
                    dos.writeUTF("Producto A");     // 2 bytes (len) + 10 bytes (txt) = 12 bytes
                    dos.writeDouble(99.99);         // 8 bytes
                    dos.writeBoolean(true);         // 1 byte
                    dos.writeInt(200);              // 4 bytes
                }                                   // Total estimado: 29 bytes

                // 2. Analizar
                Reporte reporte = analizarArchivoBinario(archivoBinario);

                // 3. Mostrar
                mostrarReporte(reporte);

                // 4. Guardar
                guardarReporte(reporte, archivoReporte);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

// --- CLASES AUXILIARES (POJOs) ---

    class Reporte {
        private String nombreArchivo;
        private long tamañoBytes;
        private List<ElementoDato> elementos;
        private int totalInts;
        private int totalDoubles;
        private int totalStrings;

        public Reporte(String nombreArchivo, long tamañoBytes) {
            this.nombreArchivo = nombreArchivo;
            this.tamañoBytes = tamañoBytes;
            this.elementos = new ArrayList<>();
            this.totalInts = 0;
            this.totalDoubles = 0;
            this.totalStrings = 0;
        }

        public void agregarElemento(ElementoDato e) {
            this.elementos.add(e);
        }

        public void incrementarInts() { totalInts++; }
        public void incrementarDoubles() { totalDoubles++; }
        public void incrementarStrings() { totalStrings++; }

        // Getters
        public String getNombreArchivo() { return nombreArchivo; }
        public long getTamañoBytes() { return tamañoBytes; }
        public List<ElementoDato> getElementos() { return elementos; }
        public int getTotalInts() { return totalInts; }
        public int getTotalDoubles() { return totalDoubles; }
        public int getTotalStrings() { return totalStrings; }
    }

    class ElementoDato {
        private int startPos;
        private int endPos;
        private String tipo;
        private String valor;

        public ElementoDato(int startPos, int endPos, String tipo, String valor) {
            this.startPos = startPos;
            this.endPos = endPos;
            this.tipo = tipo;
            this.valor = valor;
        }

        @Override
        public String toString() {
            return String.format(" [Pos %d-%d] %s: %s", startPos, endPos, tipo, valor);
        }
    }

