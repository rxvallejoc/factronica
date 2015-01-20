package com.obiectumclaro.factronica.core.emac.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "DOCUMENTO_SRI")
@XmlRootElement
@NamedQueries({@javax.persistence.NamedQuery(name = "DocumentoSri.findAll", query = "SELECT d FROM DocumentoSri d"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByNumeroSecuencia", query = "SELECT d FROM DocumentoSri d WHERE d.documentoSriPK.numeroSecuencia = :numeroSecuencia"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByCodigoEmpresa", query = "SELECT d FROM DocumentoSri d WHERE d.documentoSriPK.codigoEmpresa = :codigoEmpresa"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByRuc", query = "SELECT d FROM DocumentoSri d WHERE d.ruc = :ruc"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByCoddoc", query = "SELECT d FROM DocumentoSri d WHERE d.coddoc = :coddoc"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByEstab", query = "SELECT d FROM DocumentoSri d WHERE d.estab = :estab"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByPtoemi", query = "SELECT d FROM DocumentoSri d WHERE d.ptoemi = :ptoemi"), @javax.persistence.NamedQuery(name = "DocumentoSri.findBySecuencial", query = "SELECT d FROM DocumentoSri d WHERE d.secuencial = :secuencial"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByReferenciadocumento", query = "SELECT d FROM DocumentoSri d WHERE d.documentoSriPK.referenciadocumento = :referenciadocumento"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByEstado", query = "SELECT d FROM DocumentoSri d WHERE d.estado = :estado"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByNumeroautorizacion", query = "SELECT d FROM DocumentoSri d WHERE d.numeroautorizacion = :numeroautorizacion"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByFechaautorizacion", query = "SELECT d FROM DocumentoSri d WHERE d.fechaautorizacion = :fechaautorizacion"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByAmbiente", query = "SELECT d FROM DocumentoSri d WHERE d.ambiente = :ambiente"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByTipoemision", query = "SELECT d FROM DocumentoSri d WHERE d.tipoemision = :tipoemision"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByRazonsocial", query = "SELECT d FROM DocumentoSri d WHERE d.razonsocial = :razonsocial"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByNombrecomercial", query = "SELECT d FROM DocumentoSri d WHERE d.nombrecomercial = :nombrecomercial"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByClaveacceso", query = "SELECT d FROM DocumentoSri d WHERE d.claveacceso = :claveacceso"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByDirmatriz", query = "SELECT d FROM DocumentoSri d WHERE d.dirmatriz = :dirmatriz"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByFechaemision", query = "SELECT d FROM DocumentoSri d WHERE d.fechaemision = :fechaemision"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByDirestablecimiento", query = "SELECT d FROM DocumentoSri d WHERE d.direstablecimiento = :direstablecimiento"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByContribuyenteespecia", query = "SELECT d FROM DocumentoSri d WHERE d.contribuyenteespecia = :contribuyenteespecia"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByObligadoacontabili", query = "SELECT d FROM DocumentoSri d WHERE d.obligadoacontabili = :obligadoacontabili"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByTipoidentificacion", query = "SELECT d FROM DocumentoSri d WHERE d.tipoidentificacion = :tipoidentificacion"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByGuiaremision", query = "SELECT d FROM DocumentoSri d WHERE d.guiaremision = :guiaremision"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByRazonsocial1", query = "SELECT d FROM DocumentoSri d WHERE d.razonsocial1 = :razonsocial1"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByIdentificacion", query = "SELECT d FROM DocumentoSri d WHERE d.identificacion = :identificacion"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByTotalsinimpuestos", query = "SELECT d FROM DocumentoSri d WHERE d.totalsinimpuestos = :totalsinimpuestos"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByTotaldescuento", query = "SELECT d FROM DocumentoSri d WHERE d.totaldescuento = :totaldescuento"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByPropina", query = "SELECT d FROM DocumentoSri d WHERE d.propina = :propina"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByImportetotal", query = "SELECT d FROM DocumentoSri d WHERE d.importetotal = :importetotal"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByMoneda", query = "SELECT d FROM DocumentoSri d WHERE d.moneda = :moneda"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByCampoadicionalnombr", query = "SELECT d FROM DocumentoSri d WHERE d.campoadicionalnombr = :campoadicionalnombr"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByPeriodofiscal", query = "SELECT d FROM DocumentoSri d WHERE d.periodofiscal = :periodofiscal"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByConveniodobletribu", query = "SELECT d FROM DocumentoSri d WHERE d.conveniodobletribu = :conveniodobletribu"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByContribuyenterise", query = "SELECT d FROM DocumentoSri d WHERE d.contribuyenterise = :contribuyenterise"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByCodigodocumentomod", query = "SELECT d FROM DocumentoSri d WHERE d.codigodocumentomod = :codigodocumentomod"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByNumerodocumentomod", query = "SELECT d FROM DocumentoSri d WHERE d.numerodocumentomod = :numerodocumentomod"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByFechaemisiondocume", query = "SELECT d FROM DocumentoSri d WHERE d.fechaemisiondocume = :fechaemisiondocume"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByMotivodelanota", query = "SELECT d FROM DocumentoSri d WHERE d.motivodelanota = :motivodelanota"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByValormotivo", query = "SELECT d FROM DocumentoSri d WHERE d.valormotivo = :valormotivo"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByValormodificacion", query = "SELECT d FROM DocumentoSri d WHERE d.valormodificacion = :valormodificacion"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByTotalimpuestos", query = "SELECT d FROM DocumentoSri d WHERE d.totalimpuestos = :totalimpuestos"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByEmail", query = "SELECT d FROM DocumentoSri d WHERE d.email = :email"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByEmail1", query = "SELECT d FROM DocumentoSri d WHERE d.email1 = :email1"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByEstadoproceso", query = "SELECT d FROM DocumentoSri d WHERE d.estadoproceso = :estadoproceso"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByCodigoerror", query = "SELECT d FROM DocumentoSri d WHERE d.codigoerror = :codigoerror"), @javax.persistence.NamedQuery(name = "DocumentoSri.findByMensajeerror", query = "SELECT d FROM DocumentoSri d WHERE d.mensajeerror = :mensajeerror")})
public class DocumentoSri
        implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected DocumentoSriPK documentoSriPK;

    @Size(max = 13)
    @Column(name = "RUC")
    private String ruc;

    @Size(max = 2)
    @Column(name = "CODDOC")
    private String coddoc;

    @Size(max = 3)
    @Column(name = "ESTAB")
    private String estab;

    @Size(max = 3)
    @Column(name = "PTOEMI")
    private String ptoemi;

    @Size(max = 9)
    @Column(name = "SECUENCIAL")
    private String secuencial;

    @Size(max = 2)
    @Column(name = "ESTADO")
    private String estado;

    @Size(max = 37)
    @Column(name = "NUMEROAUTORIZACION")
    private String numeroautorizacion;

    @Size(max = 30)
    @Column(name = "FECHAAUTORIZACION")
    private String fechaautorizacion;

    @Column(name = "AMBIENTE")
    private Short ambiente;

    @Column(name = "TIPOEMISION")
    private Short tipoemision;

    @Size(max = 99)
    @Column(name = "RAZONSOCIAL")
    private String razonsocial;

    @Size(max = 99)
    @Column(name = "NOMBRECOMERCIAL")
    private String nombrecomercial;

    @Size(max = 49)
    @Column(name = "CLAVEACCESO")
    private String claveacceso;

    @Size(max = 99)
    @Column(name = "DIRMATRIZ")
    private String dirmatriz;

    @Size(max = 10)
    @Column(name = "FECHAEMISION")
    private String fechaemision;

    @Size(max = 99)
    @Column(name = "DIRESTABLECIMIENTO")
    private String direstablecimiento;

    @Size(max = 5)
    @Column(name = "CONTRIBUYENTEESPECIA")
    private String contribuyenteespecia;

    @Size(max = 2)
    @Column(name = "OBLIGADOACONTABILI")
    private String obligadoacontabili;

    @Size(max = 2)
    @Column(name = "TIPOIDENTIFICACION")
    private String tipoidentificacion;

    @Size(max = 17)
    @Column(name = "GUIAREMISION")
    private String guiaremision;

    @Size(max = 99)
    @Column(name = "RAZONSOCIAL1")
    private String razonsocial1;

    @Size(max = 13)
    @Column(name = "IDENTIFICACION")
    private String identificacion;

    @Column(name = "TOTALSINIMPUESTOS")
    private BigDecimal totalsinimpuestos;

    @Column(name = "TOTALDESCUENTO")
    private BigDecimal totaldescuento;

    @Column(name = "PROPINA")
    private BigDecimal propina;

    @Column(name = "IMPORTETOTAL")
    private BigDecimal importetotal;

    @Size(max = 15)
    @Column(name = "MONEDA")
    private String moneda;

    @Size(max = 99)
    @Column(name = "CAMPOADICIONALNOMBR")
    private String campoadicionalnombr;

    @Size(max = 7)
    @Column(name = "PERIODOFISCAL")
    private String periodofiscal;

    @Size(max = 99)
    @Column(name = "CONVENIODOBLETRIBU")
    private String conveniodobletribu;

    @Size(max = 40)
    @Column(name = "CONTRIBUYENTERISE")
    private String contribuyenterise;

    @Size(max = 2)
    @Column(name = "CODIGODOCUMENTOMOD")
    private String codigodocumentomod;

    @Size(max = 17)
    @Column(name = "NUMERODOCUMENTOMOD")
    private String numerodocumentomod;

    @Size(max = 10)
    @Column(name = "FECHAEMISIONDOCUME")
    private String fechaemisiondocume;

    @Size(max = 99)
    @Column(name = "MOTIVODELANOTA")
    private String motivodelanota;

    @Column(name = "VALORMOTIVO")
    private BigDecimal valormotivo;

    @Column(name = "VALORMODIFICACION")
    private BigDecimal valormodificacion;

    @Column(name = "TOTALIMPUESTOS")
    private BigDecimal totalimpuestos;

    @Size(max = 60)
    @Column(name = "EMAIL")
    private String email;

    @Size(max = 60)
    @Column(name = "EMAIL1")
    private String email1;

    @Size(max = 2)
    @Column(name = "ESTADOPROCESO")
    private String estadoproceso;

    @Column(name = "CODIGOERROR")
    private Short codigoerror;

    @Size(max = 99)
    @Column(name = "MENSAJEERROR")
    private String mensajeerror;

    @OneToMany(cascade = {javax.persistence.CascadeType.ALL}, mappedBy = "documentoSri")
    private List<DetalleDocumentoSri> detalleDocumentoSri;

    public DocumentoSri() {
    }

    public DocumentoSri(DocumentoSriPK documentoSriPK) {
        this.documentoSriPK = documentoSriPK;
    }

    public DocumentoSri(long numeroSecuencia, short codigoEmpresa, String referenciadocumento) {
        this.documentoSriPK = new DocumentoSriPK(numeroSecuencia, codigoEmpresa, referenciadocumento);
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public DocumentoSriPK getDocumentoSriPK() {
        return documentoSriPK;
    }

    public void setDocumentoSriPK(DocumentoSriPK documentoSriPK) {
        this.documentoSriPK = documentoSriPK;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getCoddoc() {
        return coddoc;
    }

    public void setCoddoc(String coddoc) {
        this.coddoc = coddoc;
    }

    public String getEstab() {
        return estab;
    }

    public void setEstab(String estab) {
        this.estab = estab;
    }

    public String getPtoemi() {
        return ptoemi;
    }

    public void setPtoemi(String ptoemi) {
        this.ptoemi = ptoemi;
    }

    public String getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(String secuencial) {
        this.secuencial = secuencial;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumeroautorizacion() {
        return numeroautorizacion;
    }

    public void setNumeroautorizacion(String numeroautorizacion) {
        this.numeroautorizacion = numeroautorizacion;
    }

    public String getFechaautorizacion() {
        return fechaautorizacion;
    }

    public void setFechaautorizacion(String fechaautorizacion) {
        this.fechaautorizacion = fechaautorizacion;
    }

    public Short getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(Short ambiente) {
        this.ambiente = ambiente;
    }

    public Short getTipoemision() {
        return tipoemision;
    }

    public void setTipoemision(Short tipoemision) {
        this.tipoemision = tipoemision;
    }

    public String getRazonsocial() {
        return razonsocial;
    }

    public void setRazonsocial(String razonsocial) {
        this.razonsocial = razonsocial;
    }

    public String getNombrecomercial() {
        return nombrecomercial;
    }

    public void setNombrecomercial(String nombrecomercial) {
        this.nombrecomercial = nombrecomercial;
    }

    public String getClaveacceso() {
        return claveacceso;
    }

    public void setClaveacceso(String claveacceso) {
        this.claveacceso = claveacceso;
    }

    public String getDirmatriz() {
        return dirmatriz;
    }

    public void setDirmatriz(String dirmatriz) {
        this.dirmatriz = dirmatriz;
    }

    public String getFechaemision() {
        return fechaemision;
    }

    public void setFechaemision(String fechaemision) {
        this.fechaemision = fechaemision;
    }

    public String getDirestablecimiento() {
        return direstablecimiento;
    }

    public void setDirestablecimiento(String direstablecimiento) {
        this.direstablecimiento = direstablecimiento;
    }

    public String getContribuyenteespecia() {
        return contribuyenteespecia;
    }

    public void setContribuyenteespecia(String contribuyenteespecia) {
        this.contribuyenteespecia = contribuyenteespecia;
    }

    public String getObligadoacontabili() {
        return obligadoacontabili;
    }

    public void setObligadoacontabili(String obligadoacontabili) {
        this.obligadoacontabili = obligadoacontabili;
    }

    public String getTipoidentificacion() {
        return tipoidentificacion;
    }

    public void setTipoidentificacion(String tipoidentificacion) {
        this.tipoidentificacion = tipoidentificacion;
    }

    public String getGuiaremision() {
        return guiaremision;
    }

    public void setGuiaremision(String guiaremision) {
        this.guiaremision = guiaremision;
    }

    public String getRazonsocial1() {
        return razonsocial1;
    }

    public void setRazonsocial1(String razonsocial1) {
        this.razonsocial1 = razonsocial1;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public BigDecimal getTotalsinimpuestos() {
        return totalsinimpuestos;
    }

    public void setTotalsinimpuestos(BigDecimal totalsinimpuestos) {
        this.totalsinimpuestos = totalsinimpuestos;
    }

    public BigDecimal getTotaldescuento() {
        return totaldescuento;
    }

    public void setTotaldescuento(BigDecimal totaldescuento) {
        this.totaldescuento = totaldescuento;
    }

    public BigDecimal getPropina() {
        return propina;
    }

    public void setPropina(BigDecimal propina) {
        this.propina = propina;
    }

    public BigDecimal getImportetotal() {
        return importetotal;
    }

    public void setImportetotal(BigDecimal importetotal) {
        this.importetotal = importetotal;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getCampoadicionalnombr() {
        return campoadicionalnombr;
    }

    public void setCampoadicionalnombr(String campoadicionalnombr) {
        this.campoadicionalnombr = campoadicionalnombr;
    }

    public String getPeriodofiscal() {
        return periodofiscal;
    }

    public void setPeriodofiscal(String periodofiscal) {
        this.periodofiscal = periodofiscal;
    }

    public String getConveniodobletribu() {
        return conveniodobletribu;
    }

    public void setConveniodobletribu(String conveniodobletribu) {
        this.conveniodobletribu = conveniodobletribu;
    }

    public String getContribuyenterise() {
        return contribuyenterise;
    }

    public void setContribuyenterise(String contribuyenterise) {
        this.contribuyenterise = contribuyenterise;
    }

    public String getCodigodocumentomod() {
        return codigodocumentomod;
    }

    public void setCodigodocumentomod(String codigodocumentomod) {
        this.codigodocumentomod = codigodocumentomod;
    }

    public String getNumerodocumentomod() {
        return numerodocumentomod;
    }

    public void setNumerodocumentomod(String numerodocumentomod) {
        this.numerodocumentomod = numerodocumentomod;
    }

    public String getFechaemisiondocume() {
        return fechaemisiondocume;
    }

    public void setFechaemisiondocume(String fechaemisiondocume) {
        this.fechaemisiondocume = fechaemisiondocume;
    }

    public String getMotivodelanota() {
        return motivodelanota;
    }

    public void setMotivodelanota(String motivodelanota) {
        this.motivodelanota = motivodelanota;
    }

    public BigDecimal getValormotivo() {
        return valormotivo;
    }

    public void setValormotivo(BigDecimal valormotivo) {
        this.valormotivo = valormotivo;
    }

    public BigDecimal getValormodificacion() {
        return valormodificacion;
    }

    public void setValormodificacion(BigDecimal valormodificacion) {
        this.valormodificacion = valormodificacion;
    }

    public BigDecimal getTotalimpuestos() {
        return totalimpuestos;
    }

    public void setTotalimpuestos(BigDecimal totalimpuestos) {
        this.totalimpuestos = totalimpuestos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEstadoproceso() {
        return estadoproceso;
    }

    public void setEstadoproceso(String estadoproceso) {
        this.estadoproceso = estadoproceso;
    }

    public Short getCodigoerror() {
        return codigoerror;
    }

    public void setCodigoerror(Short codigoerror) {
        this.codigoerror = codigoerror;
    }

    public String getMensajeerror() {
        return mensajeerror;
    }

    public void setMensajeerror(String mensajeerror) {
        this.mensajeerror = mensajeerror;
    }

    public List<DetalleDocumentoSri> getDetalleDocumentoSri() {
        return detalleDocumentoSri;
    }

    public int hashCode() {
        int hash = 0;
        hash += (this.documentoSriPK != null ? this.documentoSriPK.hashCode() : 0);
        return hash;
    }

    public boolean equals(Object object) {
        if (!(object instanceof DocumentoSri)) {
            return false;
        }
        DocumentoSri other = (DocumentoSri) object;
        if (((this.documentoSriPK == null) && (other.documentoSriPK != null)) || ((this.documentoSriPK != null) && (!this.documentoSriPK.equals(other.documentoSriPK)))) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "DocumentoSri[ documentoSriPK=" + this.documentoSriPK + " ]";
    }
}
