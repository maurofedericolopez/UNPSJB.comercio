package comercio.controladores;

import comercio.ComercioApp;
import comercio.controladoresJPA.PrecioAnteriorJpaController;
import comercio.controladoresJPA.ProductoJpaController;
import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;

/**
 *
 * @author Mauro Federico Lopez
 */
public class ProductosController extends Observable {

    private ProductoJpaController controladorProductos;
    private PrecioAnteriorJpaController controladorPrecios;

    public ProductosController() {
        super();
        controladorProductos = new ProductoJpaController(ComercioApp.getEntityManager());
        controladorPrecios = new PrecioAnteriorJpaController(ComercioApp.getEntityManager());
        
    }

    public ArrayList<Producto> obtenerProductos() {
        ArrayList<Producto> productos = new ArrayList();
        Object[] productoEntities = getControladorProductos().findProductoEntities().toArray();
        for(int i = 0; i < productoEntities.length; i++)
            productos.add((Producto) productoEntities[i]);
        return productos;
    }

    public void editarCodigoProducto(Producto producto, Object aValue) throws NonexistentEntityException, Exception {
        producto.setCodigo(String.valueOf(aValue));
        getControladorProductos().edit(producto);
        setChanged();
        notifyObservers();
    }

    public void editarDescripcionProducto(Producto producto, Object aValue) throws NonexistentEntityException, Exception {
        producto.setDescripcion(String.valueOf(aValue));
        getControladorProductos().edit(producto);
        setChanged();
        notifyObservers();
    }

    public void editarMarcaProducto(Producto producto, Object aValue) throws NonexistentEntityException, Exception {
        producto.setMarca((Marca) aValue);
        getControladorProductos().edit(producto);
        setChanged();
        notifyObservers();
    }

    public void editarOrigenProducto(Producto producto, Object aValue) throws NonexistentEntityException, Exception {
        producto.setOrigen((Origen) aValue);
        getControladorProductos().edit(producto);
        setChanged();
        notifyObservers();
    }

    public void editarUnidadProducto(Producto producto, Object aValue) throws NonexistentEntityException, Exception {
        producto.setUnidad((Unidad) aValue);
        getControladorProductos().edit(producto);
        setChanged();
        notifyObservers();
    }

    public void editarCategoriaProducto(Producto producto, Object aValue) throws NonexistentEntityException, Exception {
        producto.setCategoria((Categoria) aValue);
        getControladorProductos().edit(producto);
        setChanged();
        notifyObservers();
    }

    public void registrarNuevoProducto(String codigo, String descripcion, Object precio, Object marca, Object origen, Object unidad, Object categoria) throws NullPointerException {
        Producto nuevoProducto = new Producto();
        nuevoProducto.setCodigo(codigo.toUpperCase());
        nuevoProducto.setDescripcion(descripcion.toUpperCase());

        PrecioAnterior nuevoPrecio = new PrecioAnterior();
        nuevoPrecio.setValor(Double.valueOf(String.valueOf(precio)));
        nuevoPrecio.setFecha(new Date(new Date().getTime()));
        getControladorPrecios().create(nuevoPrecio);

        nuevoProducto.getPreciosAnteriores().add(nuevoPrecio);
        nuevoProducto.setPrecioActual(nuevoPrecio.getValor());
        nuevoProducto.setMarca((Marca) marca);
        nuevoProducto.setOrigen((Origen) origen);
        nuevoProducto.setUnidad((Unidad) unidad);
        nuevoProducto.setCategoria((Categoria) categoria);
        getControladorProductos().create(nuevoProducto);
        setChanged();
        notifyObservers();
    }

    public void eliminarProducto(Producto producto) throws NonexistentEntityException {
        getControladorProductos().destroy(producto.getIdProducto());
        setChanged();
        notifyObservers();
    }

    /**
     * @return the controladorProductos
     */
    public ProductoJpaController getControladorProductos() {
        return controladorProductos;
    }

    /**
     * @param controladorProductos the controladorProductos to set
     */
    public void setControladorProductos(ProductoJpaController controladorProductos) {
        this.controladorProductos = controladorProductos;
    }

    /**
     * @return the controladorPrecios
     */
    public PrecioAnteriorJpaController getControladorPrecios() {
        return controladorPrecios;
    }

    /**
     * @param controladorPrecios the controladorPrecios to set
     */
    public void setControladorPrecios(PrecioAnteriorJpaController controladorPrecios) {
        this.controladorPrecios = controladorPrecios;
    }

}
