package comercio.controladores;

import comercio.ComercioApp;
import comercio.controladoresJPA.ItemVentaJpaController;
import comercio.controladoresJPA.MedioDePagoJpaController;
import comercio.controladoresJPA.VentaJpaController;
import comercio.modelo.ItemVenta;
import comercio.modelo.MedioDePago;
import comercio.modelo.Producto;
import comercio.modelo.Venta;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;

/**
 *
 * @author Mauro Federico Lopez
 */
public class VentasController extends Observable {

    private VentaJpaController ventaJpaController;
    private MedioDePagoJpaController medioPagoJpaController;
    private ItemVentaJpaController itemVentaJpaController;
    private Venta venta;
    private ArrayList<ItemVenta> itemsDeVenta;

    public VentasController() {
        ventaJpaController = new VentaJpaController(ComercioApp.getEntityManager());
        medioPagoJpaController = new MedioDePagoJpaController(ComercioApp.getEntityManager());
        itemVentaJpaController = new ItemVentaJpaController(ComercioApp.getEntityManager());
        venta = new Venta();
        itemsDeVenta = new ArrayList();
    }

    /**
     * @return the ventaJpaController
     */
    public VentaJpaController getVentaJpaController() {
        return ventaJpaController;
    }

    /**
     * @param ventaJpaController the ventaJpaController to set
     */
    public void setVentaJpaController(VentaJpaController ventaJpaController) {
        this.ventaJpaController = ventaJpaController;
    }

    /**
     * @return the itemVentaJpaController
     */
    public ItemVentaJpaController getItemVentaJpaController() {
        return itemVentaJpaController;
    }

    /**
     * @param itemVentaJpaController the itemVentaJpaController to set
     */
    public void setItemVentaJpaController(ItemVentaJpaController itemVentaJpaController) {
        this.itemVentaJpaController = itemVentaJpaController;
    }

    /**
     * @return the venta
     */
    public Venta getVenta() {
        return venta;
    }

    /**
     * @param venta the venta to set
     */
    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    /**
     * @return the itemsDeVenta
     */
    public ArrayList<ItemVenta> getItemsDeVenta() {
        return itemsDeVenta;
    }

    /**
     * @param itemsDeVenta the itemsDeVenta to set
     */
    public void setItemsDeVenta(ArrayList<ItemVenta> itemsDeVenta) {
        this.itemsDeVenta = itemsDeVenta;
    }

    public void agregarItemDeVenta(ItemVenta itemDeVenta) {
        String codigoBuscado = itemDeVenta.getProducto().getCodigo();
        Iterator<ItemVenta> i = itemsDeVenta.iterator();
        Boolean encontrado = false;
        while(i.hasNext()) {
            ItemVenta item = i.next();
            if(item.getProducto().getCodigo().equals(codigoBuscado)) {
                Double cantidad = item.getCantidad() + itemDeVenta.getCantidad();
                item.setCantidad(cantidad);
                encontrado = true;
                break;
            }
        }
        if(encontrado == false)
            itemsDeVenta.add(itemDeVenta);
        setChanged();
        notifyObservers();
    }

    public ArrayList<MedioDePago> obtenerMediosDePago() {
        ArrayList<MedioDePago> mediosDePago = new ArrayList();
        Object[] array = medioPagoJpaController.findMedioPagoEntities().toArray();
        for(int i = 0; i < array.length; i++)
            mediosDePago.add((MedioDePago) array[i]);
        return mediosDePago;
    }

    public void eliminarItemDeVenta(int itemSeleccionado) throws Exception {
        if(itemSeleccionado >= 0) {
            itemsDeVenta.remove(itemSeleccionado);
            setChanged();
            notifyObservers();
        } else {
            throw new Exception("No ha seleccionado ningún item.");
        }
    }

    public void cancelarVenta() {
        venta = new Venta();
        itemsDeVenta = new ArrayList();
        setChanged();
        notifyObservers();
    }

}
