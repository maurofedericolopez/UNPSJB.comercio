package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Mauro Federico Lopez
 */
@Entity
public class Almacen implements Serializable {
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

    public Almacen() {}

    @Id
    @Column(name = "idAlmacen")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long numero;
    @ManyToOne
    private Sucursal sucursal;
    @OneToMany(mappedBy = "almacen")
    private List<LoteAlmacenado> lotesAlmacenados = new ArrayList();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Almacen)) {
            return false;
        }
        Almacen other = (Almacen) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Almacén N°" + getNumero() + " de la Sucursal N°" + getSucursal().getNumero();
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
     * @return the numero
     */
    public Long getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(Long numero) {
        this.numero = numero;
    }

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

    /**
     * @return the lotesAlmacenados
     */
    public List<LoteAlmacenado> getLotesAlmacenados() {
        return lotesAlmacenados;
    }

    /**
     * @param lotesAlmacenados the lotesAlmacenados to set
     */
    public void setLotesAlmacenados(List<LoteAlmacenado> lotesAlmacenados) {
        this.lotesAlmacenados = lotesAlmacenados;
    }

}
