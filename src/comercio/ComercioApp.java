package comercio;



import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import vistas.GestionGerenciaUI;
import vistas.GestionInventarioUI;
import vistas.GestionVentaUI;

/**
 *
 * @author Mauro Federico Lopez
 */
public class ComercioApp {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManagerFactory entityManagerFactoryEmpleados;
    private static GestionInventarioUI ventanaInventario;
    private static GestionVentaUI ventanaGestionVenta;
    private static GestionGerenciaUI ventanaGerencia;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        setEntityManagerFactory(Persistence.createEntityManagerFactory("ComercioPU"));

        ventanaInventario = new GestionInventarioUI();
        ventanaInventario.setVisible(true);

        ventanaGerencia = new GestionGerenciaUI();
        ventanaGerencia.setVisible(true);

        ventanaGestionVenta = new GestionVentaUI();
        ventanaGestionVenta.setVisible(true);
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
    public static GestionGerenciaUI getVentanaGerencia() {
        return ventanaGerencia;
    }

    /**
     * @param aVentanaGerencia the ventanaGerencia to set
     */
    public static void setVentanaGerencia(GestionGerenciaUI aVentanaGerencia) {
        ventanaGerencia = aVentanaGerencia;
    }

    /**
     * @return the entityManagerFactoryEmpleados
     */
    public static EntityManagerFactory getEntityManagerFactoryEmpleados() {
        return entityManagerFactoryEmpleados;
    }

    /**
     * @param aEntityManagerFactoryEmpleados the entityManagerFactoryEmpleados to set
     */
    public static void setEntityManagerFactoryEmpleados(EntityManagerFactory aEntityManagerFactoryEmpleados) {
        entityManagerFactoryEmpleados = aEntityManagerFactoryEmpleados;
    }

}
