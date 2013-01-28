package comercio.modelo;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Mauro Federico Lopez
 */
@Entity
public class Transferencia implements Serializable {
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
    @Column(name = "idTransferencia")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Almacen almacenOrigen;
    @ManyToOne
    private Almacen almacenDestino;
    @ManyToOne
    private PuntoVenta puntoDeVentaDestino;
    @ManyToOne
    private Lote lote;
    private Double cantidad;

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
     * @return the almacenOrigen
     */
    public Almacen getAlmacenOrigen() {
        return almacenOrigen;
    }

    /**
     * @param almacenOrigen the almacenOrigen to set
     */
    public void setAlmacenOrigen(Almacen almacenOrigen) {
        this.almacenOrigen = almacenOrigen;
    }

    /**
     * @return the almacenDestino
     */
    public Almacen getAlmacenDestino() {
        return almacenDestino;
    }

    /**
     * @param almacenDestino the almacenDestino to set
     */
    public void setAlmacenDestino(Almacen almacenDestino) {
        this.almacenDestino = almacenDestino;
    }

    /**
     * @return the puntoDeVentaDestino
     */
    public PuntoVenta getPuntoDeVentaDestino() {
        return puntoDeVentaDestino;
    }

    /**
     * @param puntoDeVentaDestino the puntoDeVentaDestino to set
     */
    public void setPuntoDeVentaDestino(PuntoVenta puntoDeVentaDestino) {
        this.puntoDeVentaDestino = puntoDeVentaDestino;
    }

    /**
     * @return the cantidad
     */
    public Double getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transferencia)) {
            return false;
        }
        Transferencia other = (Transferencia) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "comercio.modelo.Transferencia[ id=" + getId() + " ]";
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

}
