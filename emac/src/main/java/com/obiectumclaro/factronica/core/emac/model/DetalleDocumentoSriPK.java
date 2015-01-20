package com.obiectumclaro.factronica.core.emac.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Embeddable
public class DetalleDocumentoSriPK
        implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGO_EMPRESA")
    private short codigoEmpresa;

    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMERO_SECUENCIA")
    private long numeroSecuencia;

    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private long secuencia;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "REFERENCIADOCUMENTO")
    private String referenciadocumento;

    public DetalleDocumentoSriPK() {
    }

    public DetalleDocumentoSriPK(short codigoEmpresa, long numeroSecuencia, long secuencia, String referenciadocumento) {
        this.codigoEmpresa = codigoEmpresa;
        this.numeroSecuencia = numeroSecuencia;
        this.secuencia = secuencia;
        this.referenciadocumento = referenciadocumento;
    }

    public short getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(short codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public long getNumeroSecuencia() {
        return numeroSecuencia;
    }

    public void setNumeroSecuencia(long numeroSecuencia) {
        this.numeroSecuencia = numeroSecuencia;
    }

    public long getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(long secuencia) {
        this.secuencia = secuencia;
    }

    public String getReferenciadocumento() {
        return referenciadocumento;
    }

    public void setReferenciadocumento(String referenciadocumento) {
        this.referenciadocumento = referenciadocumento;
    }

    public int hashCode() {
        int hash = 0;
        hash += this.codigoEmpresa;
        hash += (int) this.numeroSecuencia;
        hash += (int) this.secuencia;
        hash += (this.referenciadocumento != null ? this.referenciadocumento.hashCode() : 0);
        return hash;
    }

    public boolean equals(Object object) {
        if (!(object instanceof DetalleDocumentoSriPK)) {
            return false;
        }
        DetalleDocumentoSriPK other = (DetalleDocumentoSriPK) object;
        if (this.codigoEmpresa != other.codigoEmpresa) {
            return false;
        }
        if (this.numeroSecuencia != other.numeroSecuencia) {
            return false;
        }
        if (this.secuencia != other.secuencia) {
            return false;
        }
        if (((this.referenciadocumento == null) && (other.referenciadocumento != null)) || ((this.referenciadocumento != null) && (!this.referenciadocumento.equals(other.referenciadocumento)))) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "DetalleDocumentoSriPK[ codigoEmpresa=" + this.codigoEmpresa + ", numeroSecuencia=" + this.numeroSecuencia + ", secuencia=" + this.secuencia + ", referenciadocumento=" + this.referenciadocumento + " ]";
    }
}
