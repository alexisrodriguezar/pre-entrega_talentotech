package model;

import util.ValidadorPedido;

public class LineaPedido {

    private Producto producto;
    private String nombreProducto;
    private double precioUnitario;
    private int cantidad;

    public LineaPedido(Producto producto, int cantidad) {
        ValidadorPedido.validarProductoNoNulo(producto);
        ValidadorPedido.validarCantidad(cantidad);
        this.producto = producto;
        this.nombreProducto = producto.getNombre();
        this.precioUnitario = producto.getPrecio();
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        ValidadorPedido.validarCantidad(cantidad);
        this.cantidad = cantidad;
    }

    public double calcularSubtotal() {
        return precioUnitario * cantidad;
    }
}
