package comercio.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Mauro Federico Lopez
 */
@Entity
public class Venta implements Serializable {
    private static final long serialVersionUID = 1L;

    public Venta() {}

    @Id
    @Column(name = "idVenta")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long codigo;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @ManyToOne
    private MedioPago medioDePago;
    @ManyToOne
    private PuntoVenta puntoVenta;
    @OneToMany(mappedBy = "venta")
    private List<ItemVenta> items;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Venta)) {
            return false;
        }
        Venta other = (Venta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Venta[" + codigo + "]";
    }

}
