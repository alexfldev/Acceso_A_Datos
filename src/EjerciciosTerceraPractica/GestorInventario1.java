package EjerciciosTerceraPractica;
import java.io.*;
import java.util.ArrayList;
import java.util.List;



    public class GestorInventario1 {


        public static void escribirProducto(String archivo, Producto producto) throws IOException {
            // 'false' indica que NO es modo append (sobreescribe)
            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(archivo, false))) {
                guardarDatos(dos, producto);
            }
            System.out.println("Producto guardado: " + producto.getNombre());
        }


        public static void agregarProducto(String archivo, Producto producto) throws IOException {
            // 'true' indica modo append (añade al final sin borrar)
            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(archivo, true))) {
                guardarDatos(dos, producto);
            }
            System.out.println("Producto añadido: " + producto.getNombre());
        }


        private static void guardarDatos(DataOutputStream dos, Producto producto) throws IOException {
            dos.writeInt(producto.getId());
            dos.writeUTF(producto.getNombre());
            dos.writeDouble(producto.getPrecio());
            dos.writeInt(producto.getStock());
        }


        public static List<Producto> leerProductos(String archivo) throws IOException {
            List<Producto> listaProductos = new ArrayList<>();
            File file = new File(archivo);

            // Validación: si el archivo no existe, retornamos lista vacía para evitar error
            if (!file.exists()) {
                System.out.println("El archivo no existe aún.");
                return listaProductos;
            }

            try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
                while (true) {
                    // Leemos en el MISMO orden que escribimos
                    int id = dis.readInt();
                    String nombre = dis.readUTF();
                    double precio = dis.readDouble();
                    int stock = dis.readInt();

                    Producto p = new Producto(id, nombre, precio, stock);
                    listaProductos.add(p);
                }
            } catch (EOFException e) {
                // Fin del archivo alcanzado, salimos del bucle
            }

            return listaProductos;
        }

        // --- MAIN PARA PROBAR EL CÓDIGO ---
        public static void main(String[] args) {
            String archivo = "inventario.dat";

            try {
                // 1. Crear instancias
                Producto p1 = new Producto(1, "Laptop", 999.99, 10);
                Producto p2 = new Producto(2, "Mouse", 19.99, 50);

                // 2. Guardar (esto crea el archivo o lo sobreescribe)
                escribirProducto(archivo, p1);

                // 3. Agregar (esto añade al final)
                agregarProducto(archivo, p2);

                // 4. Leer y mostrar
                System.out.println("\n--- Leyendo inventario ---");
                List<Producto> productos = leerProductos(archivo);

                for (Producto p : productos) {
                    System.out.println(p);
                }

            } catch (IOException e) {
                System.err.println("Ocurrió un error: " + e.getMessage());
            }
        }
    }

    // --- CLASE PRODUCTO ---
// No es 'public' para poder estar en el mismo archivo que GestorInventario
    class Producto implements Serializable {
        private int id;
        private String nombre;
        private double precio;
        private int stock;

        public Producto(int id, String nombre, double precio, int stock) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
            this.stock = stock;
        }

        // Getters
        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public double getPrecio() { return precio; }
        public int getStock() { return stock; }

        @Override
        public String toString() {
            return "ID: " + id + ", Nombre: " + nombre + ", Precio: " + precio + ", Stock: " + stock;
        }
    }

