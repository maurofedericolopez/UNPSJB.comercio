package comercio;

import comercio.vistas.GestionGerencia;
import comercio.vistas.GestionInventarioUI;
import comercio.vistas.GestionVenta;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Mauro Federico Lopez
 */
public class ComercioApp {

    private static EntityManagerFactory entityManagerFactory;
    private static GestionInventarioUI ventanaInventario;
    private static GestionVenta ventanaGestionVenta;
    private static GestionGerencia ventanaGerencia;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        setEntityManagerFactory(Persistence.createEntityManagerFactory("ComercioPU"));

        ventanaInventario = new GestionInventarioUI();
        ventanaInventario.setVisible(true);

        ventanaGerencia = new GestionGerencia();
        ventanaGerencia.setVisible(true);
    }

    /**
     * @return the entityManagerFactory
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    /**
     * @param aEntityManager the entityManagerFactory to set
     */
    public static void setEntityManagerFactory(EntityManagerFactory aEntityManager) {
        entityManagerFactory = aEntityManager;
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
     * @return the ventanaGerencia
     */
    public static GestionGerencia getVentanaGerencia() {
        return ventanaGerencia;
    }

    /**
     * @param aVentanaGerencia the ventanaGerencia to set
     */
    public static void setVentanaGerencia(GestionGerencia aVentanaGerencia) {
        ventanaGerencia = aVentanaGerencia;
    }

}
