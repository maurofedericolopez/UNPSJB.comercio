package comercio.controladores;

import comercio.ComercioApp;
import comercio.controladoresJPA.PrecioAnteriorJpaController;
import comercio.controladoresJPA.ProductoJpaController;
import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

/**
 *
 * @author Mauro Federico Lopez
 */
public class ProductosController extends Observable {

    private ProductoJpaController productoJpaController;
    private PrecioAnteriorJpaController precioJpaController;

    public ProductosController() {
        super();
        productoJpaController = new ProductoJpaController(ComercioApp.getEntityManager());
        precioJpaController = new PrecioAnteriorJpaController(ComercioApp.getEntityManager());
        
    }

    public ArrayList<Producto> obtenerProductos() {
        ArrayList<Producto> productos = new ArrayList();
        Object[] productoEntities = getControladorProductos().findProductoEntities().toArray();
        for(int i = 0; i < productoEntities.length; i++)
            productos.add((Producto) productoEntities[i]);
        return productos;
    }

    public void registrarNuevoProducto(String codigo, String descripcion, Double precio, Marca marca, Origen origen, Unidad unidad, Categoria categoria) throws Exception {
        if(camposCompletos(codigo, descripcion, marca, origen, unidad, categoria)) {
            if(codigoProductoValido(codigo)) {
                Producto nuevoProducto = new Producto();
                nuevoProducto.setCodigo(codigo.toUpperCase());
                nuevoProducto.setDescripcion(descripcion.toUpperCase());

                PrecioAnterior nuevoPrecio = new PrecioAnterior();
                nuevoPrecio.setValor(Double.valueOf(String.valueOf(precio)));
                nuevoPrecio.setFecha(new Date(new Date().getTime()));
                precioJpaController.create(nuevoPrecio);

                nuevoProducto.getPreciosAnteriores().add(nuevoPrecio);
                nuevoProducto.setPrecioActual(nuevoPrecio.getValor());
                nuevoProducto.setMarca((Marca) marca);
                nuevoProducto.setOrigen((Origen) origen);
                nuevoProducto.setUnidad((Unidad) unidad);
                nuevoProducto.setCategoria((Categoria) categoria);
                productoJpaController.create(nuevoProducto);

                setChanged();
                notifyObservers();
            }
        }
    }

    public void editarProducto(Producto producto) throws Exception {
        try {
            if(camposCompletos(producto.getCodigo(), producto.getDescripcion(), producto.getMarca(), producto.getOrigen(), producto.getUnidad(), producto.getCategoria())) {
                if (codigoProductoValido(producto.getCodigo())) {
                    productoJpaController.edit(producto);
                }
            }
        } catch (NonexistentEntityException ex) {
            throw new Exception("El producto no existe.");
        }
    }

    public void eliminarProducto(Producto producto) throws NonexistentEntityException {
        productoJpaController.destroy(producto.getIdProducto());
        setChanged();
        notifyObservers();
    }

    /**
     * @return the controladorProductos
     */
    public ProductoJpaController getControladorProductos() {
        return productoJpaController;
    }

    /**
     * @param controladorProductos the controladorProductos to set
     */
    public void setControladorProductos(ProductoJpaController controladorProductos) {
        this.productoJpaController = controladorProductos;
    }

    /**
     * @return the controladorPrecios
     */
    public PrecioAnteriorJpaController getControladorPrecios() {
        return precioJpaController;
    }

    /**
     * @param controladorPrecios the controladorPrecios to set
     */
    public void setControladorPrecios(PrecioAnteriorJpaController controladorPrecios) {
        this.precioJpaController = controladorPrecios;
    }

    public Producto obtenerProductoPorCodigo(String codigoProducto) throws Exception {
        try {
            return productoJpaController.findProductoByCodigo(codigoProducto.toUpperCase());
        } catch (NoResultException ex) {
            throw new Exception("No existe ningun producto con el código ingresado.");
        } catch (NonUniqueResultException ex) {
            throw new Exception("Existen más de un producto con el código ingresado.");
        }
    }

    public Boolean codigoProductoValido(String codigo) throws Exception {
        try {
            productoJpaController.findProductoByCodigo(codigo.toUpperCase());
            throw new Exception("El código del producto ya está registrado.");
        } catch (NoResultException ex) {
            return true;
        }
    }

    private Boolean camposCompletos(String codigo, String descripcion, Marca marca, Origen origen, Unidad unidad, Categoria categoria) throws Exception {
        if(!codigo.isEmpty())
            if(!descripcion.isEmpty())
                if(marca != null)
                    if(origen != null)
                        if(unidad != null)
                            if(categoria != null)
                                return true;
                            else
                                throw new Exception("No ha seleccionado una categoría.");
                        else
                            throw new Exception("No ha seleccionado una unidad.");
                    else
                        throw new Exception("No ha seleccionado un origen.");
                else
                    throw new Exception("No ha seleccionado una marca.");
            else
                throw new Exception("No ha indicado una descripción.");
        else
            throw new Exception("No ha indicado un código.");
    }

    public Double obtenerDescuentoVigente(Producto producto) {
        return 1.0;
    }

}
