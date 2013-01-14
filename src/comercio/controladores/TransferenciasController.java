package comercio.controladores;

import comercio.ControllerSingleton;
import comercio.controladoresJPA.LoteAlmacenadoJpaController;
import comercio.controladoresJPA.ProductoEnVentaJpaController;
import comercio.modelo.*;

/**
 *
 * @author Mauro Federico Lopez
 */
public class TransferenciasController {

    private LoteAlmacenadoJpaController loteAlmacenadoJpaController;
    private ProductoEnVentaJpaController productoEnVentaJpaController;

    public TransferenciasController() {
        loteAlmacenadoJpaController = ControllerSingleton.getLoteAlmacenadoJpaController();
        productoEnVentaJpaController = ControllerSingleton.getProductoEnVentaJpaController();
    }

    public void descontarDeAlmacen(Almacen almacen, Lote lote, Double cantidad) throws Exception {
        Object[] array = almacen.getLotesAlmacenados().toArray();
        String codigoLote = lote.getCodigo();
        Boolean encontrado = false;
        for(Object o : array) {
            LoteAlmacenado la = (LoteAlmacenado) o;
            if(la.getLote().getCodigo().equals(codigoLote)) {
                if(la.getCantidad() >= cantidad) {
                    Double cantidadNueva = la.getCantidad() - cantidad;
                    la.setCantidad(cantidadNueva);
                    encontrado = true;
                    loteAlmacenadoJpaController.edit(la);
                    break;
                } else {
                    throw new Exception("No hay cantidad suficiente para satisfacer la transferencia.");
                }
            }
        }
        if(!encontrado)
            throw new Exception("El almacén no contienen ningun lote con el código de lote ingresado.");
    }

    public void aumentarStockEnVenta(PuntoVenta puntoDeVenta, Producto producto, Double cantidad) throws Exception {
        Object[] array = puntoDeVenta.getProductosEnVenta().toArray();
        Boolean encontrado = false;
        for(Object o : array) {
            ProductoEnVenta pev = (ProductoEnVenta) o;
            if(pev.getProducto().getCodigo().equals(producto.getCodigo())) {
                pev.setCantidad(pev.getCantidad() + cantidad);
                encontrado = true;
                productoEnVentaJpaController.edit(pev);
                break;
            }
        }
        if(!encontrado) {
            ProductoEnVenta nuevo = new ProductoEnVenta();
            nuevo.setProducto(producto);
            nuevo.setPuntoVenta(puntoDeVenta);
            nuevo.setCantidad(cantidad);
            productoEnVentaJpaController.create(nuevo);
        }
    }

}
