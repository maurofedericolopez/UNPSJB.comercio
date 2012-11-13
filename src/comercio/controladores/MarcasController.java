package comercio.controladores;

import comercio.ComercioApp;
import comercio.controladoresJPA.MarcaJpaController;
import comercio.controladoresJPA.exceptions.NonexistentEntityException;
import comercio.modelo.Marca;
import java.util.ArrayList;
import java.util.Observable;

/**
 *
 * @author Mauro Federico Lopez
 */
public class MarcasController extends Observable {

    private MarcaJpaController controlador;

    public MarcasController() {
        super();
        controlador = new MarcaJpaController(ComercioApp.getEntityManager());
    }

    public void registrarNuevaMarca(String nombre, String abreviacion) {
        Marca nuevaMarca = new Marca();
        nuevaMarca.setNombre(nombre.toUpperCase());
        nuevaMarca.setAbreviacion(abreviacion.toUpperCase());
        controlador.create(nuevaMarca);
        setChanged();
        notifyObservers();
    }

    public void eliminarMarca(Marca marca) throws NonexistentEntityException {
        controlador.destroy(marca.getId());
        setChanged();
        notifyObservers();
    }

    public ArrayList<Marca> obtenerMarcas() {
        ArrayList<Marca> marcas = new ArrayList();
        Object[] marcaEntities = controlador.findMarcaEntities().toArray();
        for(int i = 0; i < marcaEntities.length; i++)
            marcas.add((Marca) marcaEntities[i]);
        return marcas;
    }

    public void editarNombreMarca(Marca marca, Object aValue) throws NonexistentEntityException, Exception {
        String nuevoNombre = String.valueOf(aValue);
        marca.setNombre(nuevoNombre.toUpperCase());
        controlador.edit(marca);
        setChanged();
        notifyObservers();
    }

    public void editarAbreviacionMarca(Marca marca, Object aValue) throws NonexistentEntityException, Exception {
        String nuevaAbreviacion = String.valueOf(aValue);
        marca.setAbreviacion(nuevaAbreviacion.toUpperCase());
        controlador.edit(marca);
        setChanged();
        notifyObservers();
    }

}
