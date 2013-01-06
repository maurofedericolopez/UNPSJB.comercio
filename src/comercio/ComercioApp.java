package comercio;

import comercio.controladores.RemitosController;
import comercio.vistas.GestionInventarioUI;
import comercio.vistas.GestionVenta;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Mauro Federico Lopez
 */
public class ComercioApp {

    private static EntityManagerFactory entityManager;
    private static GestionInventarioUI ventanaInventario;
    private static GestionVenta ventanaGestionVenta;
    private static RemitosController remitosController;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        setEntityManager(Persistence.createEntityManagerFactory("ComercioPU"));

        //Se inicializan los controller
        setRemitosController(new RemitosController());

        ventanaGestionVenta = new GestionVenta();
        ventanaGestionVenta.setVisible(true);
    }

    /**
     * @return the entityManager
     */
    public static EntityManagerFactory getEntityManager() {
        return entityManager;
    }

    /**
     * @param aEntityManager the entityManager to set
     */
    public static void setEntityManager(EntityManagerFactory aEntityManager) {
        entityManager = aEntityManager;
    }

    /**
     * @return the ventanaInventario
     */
    public static GestionInventarioUI getVentanaInventario() {
        return ventanaInventario;
    }

    /**
     * @param aVentanaInventario the ventanaInventario to set
     */
    public static void setVentanaInventario(GestionInventarioUI aVentanaInventario) {
        ventanaInventario = aVentanaInventario;
    }

    /**
     * @return the remitosController
     */
    public static RemitosController getRemitosController() {
        return remitosController;
    }

    /**
     * @param aRemitosController the remitosController to set
     */
    public static void setRemitosController(RemitosController aRemitosController) {
        remitosController = aRemitosController;
    }

}
