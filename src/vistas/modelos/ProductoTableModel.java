package vistas.modelos;

import comercio.ControllerSingleton;
import controladoresJPA.ProductoJpaController;
import modelo.Producto;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Mauro Federico Lopez
 */
public class ProductoTableModel extends AbstractTableModel implements Observer {

    private String[] columnsNames = {"Código","Descripción","Precio unitario[$]","Marca","Origen","Descuento[%]","Unidad","Categoría"};
    private ProductoJpaController productoJpaController;
    private ArrayList<Producto> productos;

    public ProductoTableModel() {
        super();
        productoJpaController = ControllerSingleton.getProductoJpaController();
        productoJpaController.addObserver(this);
        productos = productoJpaController.obtenerProductos();
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Long.class;
            case 1:
                return String.class;
            case 2:
                return Double.class;
            case 3:
                return String.class;
            case 4:
                return String.class;
            case 5:
                return Double.class;
            case 6:
                return String.class;
            case 7:
                return String.class;
            default:
                return null;
        }
    }

    /**
     * Retorna el nombre de la columna señalada.
     * @param c
     * @return 
     */
    @Override
    public String getColumnName( int c ) {
        return columnsNames[c];
    }

    @Override
    public int getColumnCount() {
        return columnsNames.length;
    }

    @Override
    public int getRowCount() {
        return productos.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Producto producto = productos.get(rowIndex);
        switch (columnIndex){
            case 0 :
                return producto.getCodigo();
            case 1 :
                return producto.getDescripcion();
            case 2 :
                return producto.getPrecioActual();
            case 3 :
                return producto.getMarca().toString();
            case 4 :
                return producto.getOrigen().toString();
            case 5 :
                return productoJpaController.mostrarDescuentoVigente(producto) * 100;
            case 6 :
                return producto.getUnidad().toString();
            case 7 :
                return producto.getCategoria().toString();
            default :
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    @Override
    public void update(Observable o, Object arg) {
        productos = productoJpaController.obtenerProductos();
        fireTableDataChanged();
    }

    public Producto obtenerProducto(int index) {
        return productos.get(index);
    }

}
