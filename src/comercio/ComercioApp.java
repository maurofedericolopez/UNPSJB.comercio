package comercio;

import comercio.vistas.GestionInventarioUI;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Mauro Federico Lopez
 */
public class ComercioApp {

    private static EntityManagerFactory entityManager;
    private static GestionInventarioUI ventanaInventario;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        setEntityManager(Persistence.createEntityManagerFactory("ComercioPU"));
        GestionInventarioUI ventanaGestionInventario = new GestionInventarioUI();
        ventanaGestionInventario.setVisible(true);
        setVentanaInventario(ventanaGestionInventario);
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

}
