package controladoresJPA.exceptions;

import javax.swing.JOptionPane;

/**
 *
 * @author Mauro Federico Lopez
 */
public class CampoIncompletoException extends Exception {

    public CampoIncompletoException(String message) {
        super(message);
    }

    public void mostrarDialogo() {
        JOptionPane.showMessageDialog(null, getMessage(), "Campo Incompleto", JOptionPane.ERROR_MESSAGE);
    }

}
