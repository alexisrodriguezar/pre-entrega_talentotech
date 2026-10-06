import exception.DatoInvalidoException;
import exception.ProductoDuplicadoException;
import exception.ProductoNoEncontradoException;
import exception.StockInsuficienteException;
import model.LineaPedido;
import model.Pedido;
import model.Producto;
import service.PedidoService;
import service.ProductoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ProductoService productoService = new ProductoService();
    private static final PedidoService pedidoService = new PedidoService(productoService);

    public static void main(String[] args) {
        int opcion = 0;

        while (opcion != 7) {
            mostrarMenu();
            try {
                opcion = leerEntero("Elija una opción: ");
                ejecutarOpcion(opcion);
            } catch (NumberFormatException e) {
                System.out.println("Error: ingrese un número válido.");
            } catch (DatoInvalidoException | ProductoDuplicadoException
                     | ProductoNoEncontradoException | StockInsuficienteException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("=====================================");
        System.out.println("SISTEMA DE GESTIÓN");
        System.out.println("=====================================");
        System.out.println();
        System.out.println("1) Agregar producto");
        System.out.println("2) Listar productos");
        System.out.println("3) Buscar/Actualizar producto");
        System.out.println("4) Eliminar producto");
        System.out.println("5) Crear un pedido");
        System.out.println("6) Listar pedidos");
        System.out.println("7) Salir");
        System.out.println();
    }

    private static void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                agregarProducto();
                break;
            case 2:
                listarProductos();
                break;
            case 3:
                buscarOActualizarProducto();
                break;
            case 4:
                eliminarProducto();
                break;
            case 5:
                crearPedido();
                break;
            case 6:
                listarPedidos();
                break;
            case 7:
                System.out.println("Hasta luego!");
                break;
            default:
                System.out.println("Opción inválida: elija un número del 1 al 7.");
        }
    }

    private static void agregarProducto() {
        String nombre = leerTexto("Nombre: ");
        double precio = leerDecimal("Precio: ");
        int stock = leerEntero("Stock: ");

        Producto producto = productoService.agregarProducto(new Producto(nombre, precio, stock));
        System.out.println("Producto agregado: " + producto);
    }

    private static void listarProductos() {
        List<Producto> productos = productoService.listarProductos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
        }
        for (Producto p : productos) {
            System.out.println(p);
        }
    }

    private static void buscarOActualizarProducto() {
        int id = leerEntero("ID del producto: ");
        Producto producto = productoService.obtenerProductoPorId(id);
        System.out.println("Producto encontrado: " + producto);

        System.out.println("Ingrese los nuevos datos:");
        String nombre = leerTexto("Nuevo nombre: ");
        double precio = leerDecimal("Nuevo precio: ");
        int stock = leerEntero("Nuevo stock: ");

        Producto actualizado = productoService.actualizarProducto(id, new Producto(nombre, precio, stock));
        System.out.println("Producto actualizado: " + actualizado);
    }

    private static void eliminarProducto() {
        int id = leerEntero("ID del producto a eliminar: ");
        productoService.eliminarProducto(id);
        System.out.println("Producto eliminado.");
    }

    private static void crearPedido() {
        List<LineaPedido> carrito = new ArrayList<>();

        while (true) {
            try {
                int id = leerEntero("ID del producto (0 para terminar): ");
                if (id == 0) {
                    break;
                }
                int cantidad = leerEntero("Cantidad: ");
                pedidoService.agregarAlCarrito(carrito, id, cantidad);
                System.out.println("Producto agregado al pedido.");
            } catch (NumberFormatException e) {
                System.out.println("Error: ingrese un número válido.");
            } catch (DatoInvalidoException | ProductoNoEncontradoException | StockInsuficienteException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        Pedido pedido = pedidoService.procesarCompra(carrito);
        System.out.println("Pedido #" + pedido.getId() + " creado. Total: $" + pedido.calcularTotal());
    }

    private static void listarPedidos() {
        List<Pedido> pedidos = pedidoService.listarPedidos();
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
        }
        for (Pedido pedido : pedidos) {
            System.out.println("Pedido #" + pedido.getId());
            for (LineaPedido linea : pedido.getLineas()) {
                System.out.println("  " + linea.getNombreProducto() + " x" + linea.getCantidad()
                        + " = $" + linea.calcularSubtotal());
            }
            System.out.println("  Total: $" + pedido.calcularTotal());
        }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private static double leerDecimal(String mensaje) {
        System.out.print(mensaje);
        return Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
    }
}