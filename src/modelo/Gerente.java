package modelo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 *
 * @author Mauro Federico Lopez
 */
@Entity
@PrimaryKeyJoinColumn(name = "idEmpleado")
public class Gerente extends Empleado implements Serializable {

    @ManyToOne
    private Sucursal sucursal;

    /**
     * @return the sucursal
     */
    public Sucursal getSucursal() {
        return sucursal;
    }

    /**
     * @param sucursal the sucursal to set
     */
    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

}
