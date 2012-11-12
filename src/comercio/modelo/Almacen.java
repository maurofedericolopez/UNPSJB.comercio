package comercio.modelo;

import java.io.Serializable;
import java.util.HashMap;
import javax.persistence.*;

/**
 *
 * @author Mauro Federico Lopez
 */
@Entity
public class Almacen implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "idAlmacen")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long numero;
    @ManyToOne
    private Sucursal sucursal;
    @JoinTable(name = "lotealmacenado",
            joinColumns = @JoinColumn(name = "Almacen_idAlmacen"),
            inverseJoinColumns = @JoinColumn(name = "Lote_idLote"))
    @OneToMany
    private HashMap<Lote, Double> lotes = new HashMap();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Almacen)) {
            return false;
        }
        Almacen other = (Almacen) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Almacen [" + numero + "]";
    }

}
