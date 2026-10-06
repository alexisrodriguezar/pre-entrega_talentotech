package util;

import exception.DatoInvalidoException;
import model.LineaPedido;
import model.Producto;
import java.util.List;

public class ValidadorPedido {

    public static void validarCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new DatoInvalidoException("La cantidad solicitada debe ser mayor a cero.");
        }
    }

    public static void validarPedidoNoVacio(List<LineaPedido> lineas) {
        if (lineas == null || lineas.isEmpty()) {
            throw new DatoInvalidoException("No se puede crear un pedido sin productos.");
        }
    }

    public static void validarProductoNoNulo(Producto producto) {
        if (producto == null) {
            throw new DatoInvalidoException("El producto no puede ser nulo.");
        }
    }
}