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
public class Vendedor extends Empleado implements Serializable {

    @ManyToOne
    private PuntoVenta puntoVenta;

    /**
     * @return the puntoVenta
     */
    public PuntoVenta getPuntoVenta() {
        return puntoVenta;
    }

    /**
     * @param puntoVenta the puntoVenta to set
     */
    public void setPuntoVenta(PuntoVenta puntoVenta) {
        this.puntoVenta = puntoVenta;
    }

}
