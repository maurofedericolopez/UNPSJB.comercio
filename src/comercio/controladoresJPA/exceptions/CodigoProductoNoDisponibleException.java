package comercio.controladoresJPA.exceptions;

import javax.swing.JOptionPane;

/**
 *
 * @author Mauro Federico Lopez
 */
public class CodigoProductoNoDisponibleException extends Exception {

    public CodigoProductoNoDisponibleException() {
        super("El código del producto ingresado ya está registrado.");
    }

    public void mostrarDialogoDeError() {
        JOptionPane.showMessageDialog(null, getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

}
