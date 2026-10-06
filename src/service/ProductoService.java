package service;

import exception.DatoInvalidoException;
import exception.ProductoDuplicadoException;
import exception.ProductoNoEncontradoException;
import model.Producto;
import util.ValidadorProducto;

import java.util.ArrayList;
import java.util.List;

public class ProductoService {

    private List<Producto> productos = new ArrayList<>();

    private static int contadorId = 1;

    public Producto agregarProducto (Producto p){
        if (p == null){
            throw new DatoInvalidoException("El producto no puede ser nulo");
        }
        ValidadorProducto.validarNombre(p.getNombre());
        validarDuplicado(p.getNombre());
        ValidadorProducto.validarPrecio(p.getPrecio());
        ValidadorProducto.validarStock(p.getStock());

        p.setNombre(p.getNombre().trim());
        p.setId(contadorId);
        contadorId++;

        productos.add(p);

        return p;
    }

    public List<Producto> listarProductos(){
        return productos;
    }

    public Producto obtenerProductoPorId(int id){
        for (Producto p: productos){
            if(p.getId() == id){
                return p;
            }
        }
        throw new ProductoNoEncontradoException("No se encontró un producto con el id: " + id);
    }

    public List<Producto> buscarProductosPorNombre(String texto){
        ValidadorProducto.validarNombre(texto);
        String buscado = texto.trim().toLowerCase();
        List<Producto> encontrados = new ArrayList<>();
        for (Producto p : productos){
            if (p.getNombre().toLowerCase().contains(buscado)){
                encontrados.add(p);
            }
        }
        if (encontrados.isEmpty()){
            throw new ProductoNoEncontradoException("No se encontraron productos que coincidan con: " + texto.trim());
        }
        return encontrados;
    }

    public Producto actualizarProducto(int id, Producto datos){

        Producto p = obtenerProductoPorId(id);

        ValidadorProducto.validarNombre(datos.getNombre());
        validarDuplicado(datos.getNombre());
        ValidadorProducto.validarPrecio(datos.getPrecio());
        ValidadorProducto.validarStock(datos.getStock());

        p.setNombre(datos.getNombre().trim());
        p.setPrecio(datos.getPrecio());
        p.setStock(datos.getStock());

        return p;
    }

    public void eliminarProducto(int id){
        Producto p = obtenerProductoPorId(id);
        productos.remove(p);
    }

    private void validarDuplicado (String nombre){
        for (Producto producto: productos){
            if (producto.getNombre().equalsIgnoreCase(nombre.trim())){
                throw new ProductoDuplicadoException(nombre.trim());
            }
        }
    }
}