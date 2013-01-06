package comercio.modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author Mauro Federico Lopez
 */
@Entity
public class PuntoVenta implements Serializable {
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

    public PuntoVenta() {}

    @Id
    @Column(name = "idPuntoVenta")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long numero;
    @ManyToOne
    private Sucursal sucursal;
    @OneToMany(mappedBy = "puntoVenta")
    private List<ProductoEnVenta> productosEnVenta;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PuntoVenta)) {
            return false;
        }
        PuntoVenta other = (PuntoVenta) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "S " + sucursal.getNumero() + " - PV " + numero;
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
     * @return the productosEnVenta
     */
    public List<ProductoEnVenta> getProductosEnVenta() {
        return productosEnVenta;
    }

    /**
     * @param productosEnVenta the productosEnVenta to set
     */
    public void setProductosEnVenta(List<ProductoEnVenta> productosEnVenta) {
        this.setProductosEnVenta(productosEnVenta);
    }

}
