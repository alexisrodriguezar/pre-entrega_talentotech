package service;

import exception.StockInsuficienteException;
import model.LineaPedido;
import model.Pedido;
import model.Producto;
import util.ValidadorPedido;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PedidoService {
    private List<Pedido> historialPedidos = new ArrayList<>();
    private ProductoService productoService;
    private static int contadorIdPedido = 1;

    public PedidoService(ProductoService productoService) {
        this.productoService = productoService;
    }

    public void agregarAlCarrito(List<LineaPedido> carrito, int idProducto, int cantidad) {
        ValidadorPedido.validarCantidad(cantidad);
        Producto p = productoService.obtenerProductoPorId(idProducto);

        LineaPedido existente = buscarLinea(carrito, idProducto);
        int yaEnCarrito = (existente == null) ? 0 : existente.getCantidad();
        int disponible = p.getStock() - yaEnCarrito;
        if (cantidad > disponible) {
            throw new StockInsuficienteException("Stock insuficiente para el producto: " + p.getNombre() + ". Disponible: " + disponible);
        }

        if (existente != null) {
            existente.setCantidad(yaEnCarrito + cantidad);
        } else {
            carrito.add(new LineaPedido(p, cantidad));
        }
    }

    public Pedido procesarCompra(List<LineaPedido> lineasDelCarrito) {
        ValidadorPedido.validarPedidoNoVacio(lineasDelCarrito);
        Map<Integer, Long> cantidadPorProducto = new LinkedHashMap<>();
        for (LineaPedido linea : lineasDelCarrito) {
            cantidadPorProducto.merge(linea.getProducto().getId(), (long) linea.getCantidad(), Long::sum);
        }

        for (Map.Entry<Integer, Long> entrada : cantidadPorProducto.entrySet()) {
            Producto p = productoService.obtenerProductoPorId(entrada.getKey());
            if (p.getStock() < entrada.getValue()) {
                throw new StockInsuficienteException("Stock insuficiente para el producto: " + p.getNombre() + ". Disponible: " + p.getStock());
            }
        }

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setId(contadorIdPedido++);
        for (Map.Entry<Integer, Long> entrada : cantidadPorProducto.entrySet()) {
            Producto p = productoService.obtenerProductoPorId(entrada.getKey());
            int cantidad = entrada.getValue().intValue();
            p.setStock(p.getStock() - cantidad);
            nuevoPedido.agregarLinea(new LineaPedido(p, cantidad));
        }

        historialPedidos.add(nuevoPedido);
        return nuevoPedido;
    }

    public List<Pedido> listarPedidos() {
        return historialPedidos;
    }

    private LineaPedido buscarLinea(List<LineaPedido> carrito, int idProducto) {
        for (LineaPedido linea : carrito) {
            if (linea.getProducto().getId() == idProducto) {
                return linea;
            }
        }
        return null;
    }
}