package util;

import exception.DatoInvalidoException;

public class ValidadorProducto {

    public static void validarNombre(String nombre){
        if (nombre == null || nombre.trim().isEmpty()){
            throw new DatoInvalidoException("El nombre no puede estar vacío");
        }
    }

    public static void validarPrecio(double precio){
        if (precio <= 0){
            throw new DatoInvalidoException("El precio debe ser mayor a cero");
        }
    }

    public static void validarStock(int stock){
        if (stock<0){
            throw new DatoInvalidoException("El stock no puede ser negativo");
        }
    }
}
