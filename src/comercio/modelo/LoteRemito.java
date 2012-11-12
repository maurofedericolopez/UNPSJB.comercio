package comercio.modelo;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Mauro
 */
@Entity
public class LoteRemito implements Serializable {
    private static long serialVersionUID = 1L;

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
    @Column(name = "idLoteRemito")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Remito remito;
    @ManyToOne
    private Lote lote;
    private Double cantidadIngresada;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LoteRemito)) {
            return false;
        }
        LoteRemito other = (LoteRemito) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "comercio.modelo.LoteRemito[ id=" + getId() + " ]";
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
     * @return the remito
     */
    public Remito getRemito() {
        return remito;
    }

    /**
     * @param remito the remito to set
     */
    public void setRemito(Remito remito) {
        this.remito = remito;
    }

    /**
     * @return the lote
     */
    public Lote getLote() {
        return lote;
    }

    /**
     * @param lote the lote to set
     */
    public void setLote(Lote lote) {
        this.lote = lote;
    }

    /**
     * @return the cantidadIngresada
     */
    public Double getCantidadIngresada() {
        return cantidadIngresada;
    }

    /**
     * @param cantidadIngresada the cantidadIngresada to set
     */
    public void setCantidadIngresada(Double cantidadIngresada) {
        this.cantidadIngresada = cantidadIngresada;
    }

}
