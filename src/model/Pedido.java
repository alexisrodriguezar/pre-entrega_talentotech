package model;

import exception.DatoInvalidoException;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id;
    private List<LineaPedido> lineas;

    public Pedido() {
        this.lineas = new ArrayList<>();
    }

    public void agregarLinea(LineaPedido linea) {
        if (linea == null) {
            throw new DatoInvalidoException("La línea del pedido no puede ser nula.");
        }
        this.lineas.add(linea);
    }

    public List<LineaPedido> getLineas() { return lineas; }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public double calcularTotal() {
        double total = 0;
        for (LineaPedido linea : lineas) {
            total += linea.calcularSubtotal();
        }
        return total;
    }
}