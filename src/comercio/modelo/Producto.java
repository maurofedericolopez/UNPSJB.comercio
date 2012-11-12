package comercio.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.persistence.*;

/**
 *
 * @author Mauro Federico Lopez
 */
@Entity
public class Producto implements Serializable {
    private static long serialVersionUID = 1L;

    public Producto() {}
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
    @Column(name = "idProducto")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idProducto;
    private String codigo;
    private String descripcion;
    private Double precioActual;
    @ManyToOne
    private Marca marca;
    @ManyToOne
    private Origen origen;
    @ManyToOne
    private Oferta oferta;
    @ManyToOne
    private Unidad unidad;
    @ManyToOne
    private Categoria categoria;
    @ManyToMany
    @JoinTable(name = "precioanteriorproducto",
            joinColumns = @JoinColumn(name = "Producto_idProducto"),
            inverseJoinColumns = @JoinColumn(name = "PrecioAnterior_idPrecioAnterior"))
    private Collection<PrecioAnterior> preciosAnteriores = new ArrayList();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getIdProducto() != null ? getIdProducto().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.getIdProducto() == null && other.getIdProducto() != null) || (this.getIdProducto() != null && !this.idProducto.equals(other.idProducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "comercio.modelo.Producto[ id=" + getIdProducto() + " ]";
    }

    /**
     * @return the idProducto
     */
    public Long getIdProducto() {
        return idProducto;
    }

    /**
     * @param idProducto the idProducto to set
     */
    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
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
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the precioActual
     */
    public Double getPrecioActual() {
        return precioActual;
    }

    /**
     * @param precioActual the precioActual to set
     */
    public void setPrecioActual(Double precioActual) {
        this.precioActual = precioActual;
    }

    /**
     * @return the marca
     */
    public Marca getMarca() {
        return marca;
    }

    /**
     * @param marca the marca to set
     */
    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    /**
     * @return the origen
     */
    public Origen getOrigen() {
        return origen;
    }

    /**
     * @param origen the origen to set
     */
    public void setOrigen(Origen origen) {
        this.origen = origen;
    }

    /**
     * @return the oferta
     */
    public Oferta getOferta() {
        return oferta;
    }

    /**
     * @param oferta the oferta to set
     */
    public void setOferta(Oferta oferta) {
        this.oferta = oferta;
    }

    /**
     * @return the unidad
     */
    public Unidad getUnidad() {
        return unidad;
    }

    /**
     * @param unidad the unidad to set
     */
    public void setUnidad(Unidad unidad) {
        this.unidad = unidad;
    }

    /**
     * @return the categoria
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Double getDescuentoVigente() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * @return the preciosAnteriores
     */
    public Collection<PrecioAnterior> getPreciosAnteriores() {
        return preciosAnteriores;
    }

    /**
     * @param preciosAnteriores the preciosAnteriores to set
     */
    public void setPreciosAnteriores(Collection<PrecioAnterior> preciosAnteriores) {
        this.preciosAnteriores = preciosAnteriores;
    }

}
