package comercio.controladores;

import comercio.ComercioApp;
import comercio.controladoresJPA.CategoriaJpaController;
import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.Categoria;
import java.util.ArrayList;
import java.util.Observable;

/**
 *
 * @author Mauro Federico Lopez
 */
public class CategoriasController extends Observable {

    private CategoriaJpaController controlador;

    public CategoriasController() {
        super();
        controlador = new CategoriaJpaController(ComercioApp.getEntityManager());
    }

    public ArrayList<Categoria> obtenerCategorias() {
        ArrayList<Categoria> marcas = new ArrayList();
        Object[] categoriaEntities = controlador.findCategoriaEntities().toArray();
        for(int i = 0; i < categoriaEntities.length; i++)
            marcas.add((Categoria) categoriaEntities[i]);
        return marcas;
    }

    public void editarNombreCategoria(Categoria c, Object aValue) throws NonexistentEntityException, Exception {
        String nuevoNombre = String.valueOf(aValue);
        c.setNombre(nuevoNombre.toUpperCase());
        controlador.edit(c);
        setChanged();
        notifyObservers();
    }

    public void editarDescripcionCategoria(Categoria c, Object aValue) throws NonexistentEntityException, Exception {
        String nuevaDescripcion = String.valueOf(aValue);
        c.setDescripcion(nuevaDescripcion.toLowerCase());
        controlador.edit(c);
        setChanged();
        notifyObservers();
    }

    public void registrarNuevaCategoria(String nombre, String descripcion) {
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre(nombre.toUpperCase());
        nuevaCategoria.setDescripcion(descripcion.toLowerCase());
        controlador.create(nuevaCategoria);
        setChanged();
        notifyObservers();
    }

    public void eliminarCategoria(Categoria categoria) throws NonexistentEntityException {
        controlador.destroy(categoria.getId());
        setChanged();
        notifyObservers();
    }

}
