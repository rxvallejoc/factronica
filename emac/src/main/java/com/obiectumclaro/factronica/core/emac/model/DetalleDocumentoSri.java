package com.obiectumclaro.factronica.core.emac.model;


import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "DETALLE_DOCUMENTO_SRI")
@NamedQueries({@javax.persistence.NamedQuery(name = "DetalleDocumentoSri.findAll", query = "SELECT d FROM DetalleDocumentoSri d"), @javax.persistence.NamedQuery(name = "DetalleDocumentoSri.findByCodigoEmpresa", query = "SELECT d FROM DetalleDocumentoSri d WHERE d.detalleDocumentoSriPK.codigoEmpresa = :codigoEmpresa"), @javax.persistence.NamedQuery(name = "DetalleDocumentoSri.findByNumeroSecuencia", query = "SELECT d FROM DetalleDocumentoSri d WHERE d.detalleDocumentoSriPK.numeroSecuencia = :numeroSecuencia"), @javax.persistence.NamedQuery(name = "DetalleDocumentoSri.findBySecuencia", query = "SELECT d FROM DetalleDocumentoSri d WHERE d.detalleDocumentoSriPK.secuencia = :secuencia"), @javax.persistence.NamedQuery(name = "DetalleDocumentoSri.findByReferenciadocumento", query = "SELECT d FROM DetalleDocumentoSri d WHERE d.detalleDocumentoSriPK.referenciadocumento = :referenciadocumento"), @javax.persistence.NamedQuery(name = "DetalleDocumentoSri.findByCodPrincipal", query = "SELECT d FROM DetalleDocumentoSri d WHERE d.codPrincipal = :codPrincipal"), @javax.persistence.NamedQuery(name = "DetalleDocumentoSri.findByCodAuxuliar", query = "SELECT d FROM DetalleDocumentoSri d WHERE d.codAuxuliar = :codAuxuliar"), @javax.persistence.NamedQuery(name = "DetalleDocumentoSri.findByDescripcion", query = "SELECT d FROM DetalleDocumentoSri d WHERE d.descripcion = :descripcion"), @javax.persistence.NamedQuery(name = "DetalleDocumentoSri.findByCantidad", query = "SELECT d FROM DetalleDocumentoSri d WHERE d.cantidad = :cantidad"), @javax.persistence.NamedQuery(name = "DetalleDocumentoSri.findByPrecioUnitario", query = "SELECT d FROM DetalleDocumentoSri d WHERE d.precioUnitario = :precioUnitario"), @javax.persistence.NamedQuery(name = "DetalleDocumentoSri.findByDescuento", query = "SELECT d FROM DetalleDocumentoSri d WHERE d.descuento = :descuento"), @javax.persistence.NamedQuery(name = "DetalleDocumentoSri.findByPrecioTotalSnImpuesto", query = "SELECT d FROM DetalleDocumentoSri d WHERE d.precioTotalSnImpuesto = :precioTotalSnImpuesto"), @javax.persistence.NamedQuery(name = "DetalleDocumentoSri.findByValorIva", query = "SELECT d FROM DetalleDocumentoSri d WHERE d.valorIva = :valorIva")})
@XmlRootElement
public class DetalleDocumentoSri
        implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetalleDocumentoSriPK detalleDocumentoSriPK;

    @Size(max = 25)
    @Column(name = "COD_PRINCIPAL")
    private String codPrincipal;

    @Size(max = 25)
    @Column(name = "COD_AUXULIAR")
    private String codAuxuliar;

    @Size(max = 300)
    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "CANTIDAD")
    private BigDecimal cantidad;

    @Column(name = "PRECIO_UNITARIO")
    private BigDecimal precioUnitario;

    @Column(name = "DESCUENTO")
    private BigDecimal descuento;

    @Column(name = "PRECIO_TOTAL_SN_IMPUESTO")
    private BigDecimal precioTotalSnImpuesto;

    @Column(name = "VALOR_IVA")
    private BigDecimal valorIva;

    @JoinColumns({@javax.persistence.JoinColumn(name = "CODIGO_EMPRESA", referencedColumnName = "CODIGO_EMPRESA", insertable = false, updatable = false), @javax.persistence.JoinColumn(name = "NUMERO_SECUENCIA", referencedColumnName = "NUMERO_SECUENCIA", insertable = false, updatable = false), @javax.persistence.JoinColumn(name = "REFERENCIADOCUMENTO", referencedColumnName = "REFERENCIADOCUMENTO", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private DocumentoSri documentoSri;

    public DetalleDocumentoSri() {

    }

    public DetalleDocumentoSri(DetalleDocumentoSriPK detalleDocumentoSriPK) {
        this.detalleDocumentoSriPK = detalleDocumentoSriPK;

    }

    public DetalleDocumentoSri(short codigoEmpresa, long numeroSecuencia, long secuencia, String referenciadocumento) {
        this.detalleDocumentoSriPK = new DetalleDocumentoSriPK(codigoEmpresa, numeroSecuencia, secuencia, referenciadocumento);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public DetalleDocumentoSriPK getDetalleDocumentoSriPK() {
        return detalleDocumentoSriPK;
    }

    public void setDetalleDocumentoSriPK(DetalleDocumentoSriPK detalleDocumentoSriPK) {
        this.detalleDocumentoSriPK = detalleDocumentoSriPK;
    }

    public String getCodPrincipal() {
        return codPrincipal;
    }

    public void setCodPrincipal(String codPrincipal) {
        this.codPrincipal = codPrincipal;
    }

    public String getCodAuxuliar() {
        return codAuxuliar;
    }

    public void setCodAuxuliar(String codAuxuliar) {
        this.codAuxuliar = codAuxuliar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getPrecioTotalSnImpuesto() {
        return precioTotalSnImpuesto;
    }

    public void setPrecioTotalSnImpuesto(BigDecimal precioTotalSnImpuesto) {
        this.precioTotalSnImpuesto = precioTotalSnImpuesto;
    }

    public BigDecimal getValorIva() {
        return valorIva;
    }

    public void setValorIva(BigDecimal valorIva) {
        this.valorIva = valorIva;
    }

    public DocumentoSri getDocumentoSri() {
        return documentoSri;
    }

    public void setDocumentoSri(DocumentoSri documentoSri) {
        this.documentoSri = documentoSri;
    }

    public int hashCode() {
        int hash = 0;
        hash += (this.detalleDocumentoSriPK != null ? this.detalleDocumentoSriPK.hashCode() : 0);
        return hash;

    }

    public boolean equals(Object object) {
        if (!(object instanceof DetalleDocumentoSri)) {
            return false;

        }
        DetalleDocumentoSri other = (DetalleDocumentoSri) object;
        if (((this.detalleDocumentoSriPK == null) && (other.detalleDocumentoSriPK != null)) || ((this.detalleDocumentoSriPK != null) && (!this.detalleDocumentoSriPK.equals(other.detalleDocumentoSriPK)))) {
            return false;

        }
        return true;

    }

    public String toString()

    {
        return "DetalleDocumentoSri[ detalleDocumentoSriPK=" + this.detalleDocumentoSriPK + " ]";

    }

}

