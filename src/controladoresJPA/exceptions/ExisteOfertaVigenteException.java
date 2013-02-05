package controladoresJPA.exceptions;

import javax.swing.JOptionPane;


/**
 *
 * @author Mauro Federico Lopez
 */
public class ExisteOfertaVigenteException extends Exception {

    public ExisteOfertaVigenteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExisteOfertaVigenteException(String fecha) {
        super("El producto tiene una oferta asociada en vigencia."
                + "\nÉsta vence el " + fecha
                + "\n¿Desea reemplazar la oferta?");
    }

    public Boolean mostrarDialogo() {
        int showOptionDialog = JOptionPane.showOptionDialog(null, getMessage(), "Oferta", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        return true;
    }

}
