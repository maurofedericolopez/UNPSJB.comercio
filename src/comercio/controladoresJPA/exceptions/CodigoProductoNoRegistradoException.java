package comercio.controladoresJPA.exceptions;

import javax.swing.JOptionPane;

/**
 *
 * @author Mauro Federico Lopez
 */
public class CodigoProductoNoRegistradoException extends Exception {

    public CodigoProductoNoRegistradoException() {
        super("El código del producto ingresado no está registrado.");
    }

    public void mostrarDialogo() {
        JOptionPane.showMessageDialog(null, getMessage(), "Producto", JOptionPane.ERROR_MESSAGE);
    }

}
