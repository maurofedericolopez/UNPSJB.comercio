package comercio.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
    private Long id;
    @Column(unique = true)
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
    @OneToMany(mappedBy = "producto")
    private List<PrecioAnterior> preciosAnteriores = new ArrayList();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getDescripcion();
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
    public void setId(Long idProducto) {
        this.id = idProducto;
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
        this.codigo = codigo.toUpperCase();
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
        this.descripcion = descripcion.toUpperCase();
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

    /**
     * @return the preciosAnteriores
     */
    public List<PrecioAnterior> getPreciosAnteriores() {
        return preciosAnteriores;
    }

    /**
     * @param preciosAnteriores the preciosAnteriores to set
     */
    public void setPreciosAnteriores(List<PrecioAnterior> preciosAnteriores) {
        this.setPreciosAnteriores(preciosAnteriores);
    }

}
