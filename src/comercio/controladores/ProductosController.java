package comercio.controladores;

import comercio.ComercioApp;
import comercio.controladoresJPA.ProductoJpaController;
import comercio.exceptions.NonexistentEntityException;
import comercio.modelo.*;
import java.util.ArrayList;
import java.util.Observable;

/**
 *
 * @author Mauro Federico Lopez
 */
public class ProductosController extends Observable {

    private ProductoJpaController controlador;

    public ProductosController() {
        controlador = new ProductoJpaController(ComercioApp.getEntityManager());
    }

    public ArrayList<Producto> obtenerProductos() {
        ArrayList<Producto> productos = new ArrayList();
        Producto[] productoEntities = (Producto[]) controlador.findProductoEntities().toArray();
        for(int i = 0; i < productoEntities.length; i++)
            productos.add(productoEntities[i]);
        return productos;
    }

    public void editarCodigoProducto(Producto producto, Object aValue) throws NonexistentEntityException, Exception {
        producto.setCodigo(String.valueOf(aValue));
        controlador.edit(producto);
    }

    public void editarDescripcionProducto(Producto producto, Object aValue) throws NonexistentEntityException, Exception {
        producto.setDescripcion(String.valueOf(aValue));
        controlador.edit(producto);
    }

    public void editarMarcaProducto(Producto producto, Object aValue) throws NonexistentEntityException, Exception {
        producto.setMarca((Marca) aValue);
        controlador.edit(producto);
    }

    public void editarOrigenProducto(Producto producto, Object aValue) throws NonexistentEntityException, Exception {
        producto.setOrigen((Origen) aValue);
        controlador.edit(producto);
    }

    public void editarUnidadProducto(Producto producto, Object aValue) throws NonexistentEntityException, Exception {
        producto.setUnidad((Unidad) aValue);
        controlador.edit(producto);
    }

    public void editarCategoriaProducto(Producto producto, Object aValue) throws NonexistentEntityException, Exception {
        producto.setCategoria((Categoria) aValue);
        controlador.edit(producto);
    }

    public void registrarNuevoProducto(String codigo, String descripcion, Object precio, Object marca, Object origen, Object unidad, Object categoria) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
