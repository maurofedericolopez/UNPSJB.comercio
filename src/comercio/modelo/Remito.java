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
public class Remito implements Serializable {
    private static long serialVersionUID = 1L;

    public Remito() {}

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    @Id
    @Column(name = "idRemito")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String codigo;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @OneToMany(mappedBy = "remito")
    private List<LoteRemito> lotes;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Remito)) {
            return false;
        }
        Remito other = (Remito) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Remito[ id=" + getId() + " ]";
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the lotes
     */
    public List<LoteRemito> getLotes() {
        return lotes;
    }

    /**
     * @param lotes the lotes to set
     */
    public void setLotes(List<LoteRemito> lotes) {
        this.setLotes(lotes);
    }

}
