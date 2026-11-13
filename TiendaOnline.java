import java.util.ArrayList;
import java.util.List;

/**
 * Código de ejemplo con ANTIPATRONES INTENCIONALES (solo para la tarea de la sesión 14).
 * No es un buen diseño: está hecho a propósito para que SonarQube for IDE lo marque.
 */

// ===== ANTIPATRÓN 1: GOD OBJECT =====
// Una sola clase maneja clientes, productos, stock, pagos, reportes y notificaciones.
public class TiendaOnline {

    private List<String> clientes = new ArrayList<>();
    private List<String> productos = new ArrayList<>();
    private List<Double> precios = new ArrayList<>();
    private List<Integer> stock = new ArrayList<>();
    private double totalVentas = 0;

    // ===== ANTIPATRÓN 2: LAVA FLOW (código muerto que nadie se atrevió a borrar) =====
    private String logAntiguo = "";
    private int contadorViejo = 0;

    private void recalcularInventarioViejo() {
        // TODO: arreglar esto después
        // totalVentas = totalVentas - 10;
        // for (int i = 0; i < stock.size(); i++) {
        //     stock.set(i, stock.get(i) + 1);
        // }
    }

    // --- Gestión de clientes ---
    public void registrarCliente(String nombre) {
        clientes.add(nombre);
        System.out.println("Cliente registrado: " + nombre);
    }

    // --- Gestión de productos ---
    public void registrarProducto(String nombre, double precio, int cantidad) {
        productos.add(nombre);
        precios.add(precio);
        stock.add(cantidad);
        System.out.println("Producto registrado: " + nombre);
    }

    // ===== ANTIPATRÓN 3: SPAGHETTI CODE =====
    // Demasiados if anidados, muchos parámetros y la lógica mezclada en un solo método.
    public double procesarPedido(String cliente, String producto, int cantidad,
                                 String metodoPago, boolean esVip, String cupon) {
        double total = 0;
        if (clientes.contains(cliente)) {
            if (productos.contains(producto)) {
                int i = productos.indexOf(producto);
                if (stock.get(i) >= cantidad) {
                    if (metodoPago.equals("TARJETA")) {
                        if (esVip) {
                            if (cupon != null && cupon.equals("DESC10")) {
                                total = precios.get(i) * cantidad * 0.80;
                            } else {
                                total = precios.get(i) * cantidad * 0.90;
                            }
                        } else {
                            total = precios.get(i) * cantidad;
                        }
                    } else if (metodoPago.equals("EFECTIVO")) {
                        if (esVip) {
                            total = precios.get(i) * cantidad * 0.95;
                        } else {
                            total = precios.get(i) * cantidad;
                        }
                    } else if (metodoPago.equals("TRANSFERENCIA")) {
                        total = precios.get(i) * cantidad;
                    } else {
                        System.out.println("Método de pago no válido");
                    }
                    stock.set(i, stock.get(i) - cantidad);
                    totalVentas = totalVentas + total;
                } else {
                    System.out.println("No hay stock suficiente");
                }
            } else {
                System.out.println("Producto no existe");
            }
        } else {
            System.out.println("Cliente no registrado");
        }
        return total;
    }

    // ===== ANTIPATRÓN 4: COPIAR Y PEGAR (código duplicado) =====
    public void imprimirBoletaCliente(String cliente, String producto, double total) {
        System.out.println("==============================");
        System.out.println("TIENDA ESTILOS");
        System.out.println("Cliente: " + cliente);
        System.out.println("Producto: " + producto);
        System.out.println("Total: S/ " + total);
        System.out.println("Gracias por su compra");
        System.out.println("==============================");
    }

    public void imprimirBoletaVendedor(String cliente, String producto, double total) {
        System.out.println("==============================");
        System.out.println("TIENDA ESTILOS");
        System.out.println("Cliente: " + cliente);
        System.out.println("Producto: " + producto);
        System.out.println("Total: S/ " + total);
        System.out.println("Gracias por su compra");
        System.out.println("==============================");
    }

    // --- Reportes y notificaciones (más responsabilidades en la misma clase) ---
    public void enviarNotificacion(String cliente, String mensaje) {
        System.out.println("Enviando a " + cliente + ": " + mensaje);
    }

    public void mostrarReporteVentas() {
        System.out.println("Total de ventas: S/ " + totalVentas);
    }

    public static void main(String[] args) {
        TiendaOnline tienda = new TiendaOnline();
        ClienteHelper helper = new ClienteHelper(tienda);

        helper.registrar("Ana");
        tienda.registrarProducto("Polo", 50.0, 10);

        double total = tienda.procesarPedido("Ana", "Polo", 2, "TARJETA", true, "DESC10");
        tienda.imprimirBoletaCliente("Ana", "Polo", total);
        tienda.imprimirBoletaVendedor("Ana", "Polo", total);
        tienda.mostrarReporteVentas();
    }
}

// ===== ANTIPATRÓN 5: POLTERGEIST =====
// Clase "fantasma": no tiene lógica propia, solo llama a otra clase.
// Agrega una capa inútil que oculta que todo el trabajo lo hace TiendaOnline (el God Object).
class ClienteHelper {

    private final TiendaOnline tienda;

    ClienteHelper(TiendaOnline tienda) {
        this.tienda = tienda;
    }

    void registrar(String nombre) {
        tienda.registrarCliente(nombre);
    }
}
