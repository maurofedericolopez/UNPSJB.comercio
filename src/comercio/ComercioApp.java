package comercio;



import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import vistas.AdministradorUI;
import vistas.IniciarSesionUI;

/**
 *
 * @author Mauro Federico Lopez
 */
public class ComercioApp {

    private static EntityManagerFactory entityManagerFactory;
    private static AdministradorUI administradorUI;
    private static IniciarSesionUI iniciarSesionUI;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        setEntityManagerFactory(Persistence.createEntityManagerFactory("ComercioPU"));

//        administradorUI = new AdministradorUI();
//        administradorUI.setVisible(true);

        iniciarSesionUI = new IniciarSesionUI();
        iniciarSesionUI.setVisible(true);
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

}
