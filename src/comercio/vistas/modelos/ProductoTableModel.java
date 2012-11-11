package comercio.vistas.modelos;

import comercio.controladores.ProductosController;
import comercio.exceptions.NonexistentEntityException;
import comercio.modelo.Producto;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Mauro Federico Lopez
 */
public class ProductoTableModel extends AbstractTableModel implements Observer {

    private String[] columnsNames = {"Código","Descripción","Precio unitario[$]","Marca","Origen","Descuento","Unidad","Categoría"};
    private ProductosController controlador;
    private ArrayList<Producto> productos;

    public ProductoTableModel() {
        controlador = new ProductosController();
        controlador.addObserver(this);
        productos = controlador.obtenerProductos();
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
        switch (columnIndex){
            case 0 :
                return productos.get(rowIndex).getCodigo();
            case 1 :
                return productos.get(rowIndex).getDescripcion();
            case 2 :
                return productos.get(rowIndex).getPrecioActual();
            case 3 :
                return productos.get(rowIndex).getMarca();
            case 4 :
                return productos.get(rowIndex).getOrigen();
            case 5 :
                return null;
            case 6 :
                return productos.get(rowIndex).getUnidad();
            case 7 :
                return productos.get(rowIndex).getCategoria();
            default :
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            switch (columnIndex){
                case 0 :
                    controlador.editarCodigoProducto(productos.get(rowIndex), aValue);
                    break;
                case 1 :
                    controlador.editarDescripcionProducto(productos.get(rowIndex), aValue);
                    break;
                case 2 :
                    break;
                case 3 :
                    controlador.editarMarcaProducto(productos.get(rowIndex), aValue);
                    break;
                case 4:
                    controlador.editarOrigenProducto(productos.get(rowIndex), aValue);
                    break;
                case 5:
                    break;
                case 6:
                    controlador.editarUnidadProducto(productos.get(rowIndex), aValue);
                    break;
                case 7:
                    controlador.editarCategoriaProducto(productos.get(rowIndex), aValue);
                    break;
                default:
                    break;
                }
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ProductoTableModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ProductoTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if(col == 5 || col == 2)
            return false;
        else
            return true;
    }

    @Override
    public void update(Observable o, Object arg) {
        productos = controlador.obtenerProductos();
        this.fireTableDataChanged();
    }

}
