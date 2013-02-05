package controladores;

import comercio.ComercioApp;
import controladoresJPA.GerenteJpaController;
import controladoresJPA.GestorInventarioJpaController;
import controladoresJPA.VendedorJpaController;
import modelo.Vendedor;

/**
 *
 * @author Mauro Federico Lopez
 */
public class EmpleadosController {

    private GerenteJpaController gerenteJpaController;
    private GestorInventarioJpaController gestorInventarioJpaController;
    private VendedorJpaController vendedorJpaController;

    public EmpleadosController() {
        gerenteJpaController = new GerenteJpaController(ComercioApp.getEntityManagerFactory());
        gestorInventarioJpaController = new GestorInventarioJpaController(ComercioApp.getEntityManagerFactory());
        vendedorJpaController = new VendedorJpaController(ComercioApp.getEntityManagerFactory());
    }

    public void iniciarSesionGerente(String nombreUsuario, String contraseña) {
        
    }

    public void iniciarSesionGestorInventario(String nombreUsuario, String contraseña) {
        
    }

    public Boolean iniciarSesionVendedor(String nombreUsuario, String contraseña) throws Exception {
        try {
            Vendedor vendedor = vendedorJpaController.findVendedorByNombreUsuario(nombreUsuario);
            if (vendedor.getContraseña().equals(contraseña)) {
                return true;
            } else {
                throw new Exception("La contraseña no es válida");
            }
        } catch (NullPointerException ex) {
            throw new Exception("El nombre de usuario no está registrado");
        }
    }

}
