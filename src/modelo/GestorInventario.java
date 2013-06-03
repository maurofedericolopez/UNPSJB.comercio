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
public class GestorInventario extends Empleado implements Serializable {

    @ManyToOne
    private Almacen almacen;

    /**
     * @return the almacen
     */
    public Almacen getAlmacen() {
        return almacen;
    }

    /**
     * @param almacen the almacen to set
     */
    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

}
