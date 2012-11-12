package comercio.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Mauro Federico Lopez
 */
@Entity
public class Egreso implements Serializable {
    private static final long serialVersionUID = 1L;
    @OneToMany(mappedBy = "egreso")
    private List<LoteEgresado> lotesEgresados;

    public Egreso() {}

    @Id
    @Column(name = "idEgreso")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String codigo;
    private String causaEspecial;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    private String observaciones;
    @ManyToOne
    private Almacen almacen;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Egreso)) {
            return false;
        }
        Egreso other = (Egreso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Egreso[" + codigo + "]";
    }

}
