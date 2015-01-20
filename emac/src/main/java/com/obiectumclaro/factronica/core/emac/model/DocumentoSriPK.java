package com.obiectumclaro.factronica.core.emac.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Embeddable
public class DocumentoSriPK
        implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMERO_SECUENCIA")
    private long numeroSecuencia;

    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGO_EMPRESA")
    private short codigoEmpresa;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "REFERENCIADOCUMENTO")
    private String referenciadocumento;

    public DocumentoSriPK() {
    }

    public DocumentoSriPK(long numeroSecuencia, short codigoEmpresa, String referenciadocumento) {
/* 40 */
        this.numeroSecuencia = numeroSecuencia;
/* 41 */
        this.codigoEmpresa = codigoEmpresa;
/* 42 */
        this.referenciadocumento = referenciadocumento;
    }

    public long getNumeroSecuencia() {
/* 46 */
        return this.numeroSecuencia;
    }

    public void setNumeroSecuencia(long numeroSecuencia) {
/* 50 */
        this.numeroSecuencia = numeroSecuencia;
    }

    public short getCodigoEmpresa() {
/* 54 */
        return this.codigoEmpresa;
    }

    public void setCodigoEmpresa(short codigoEmpresa) {
/* 58 */
        this.codigoEmpresa = codigoEmpresa;
    }

    public String getReferenciadocumento() {
/* 62 */
        return this.referenciadocumento;
    }

    public void setReferenciadocumento(String referenciadocumento) {
/* 66 */
        this.referenciadocumento = referenciadocumento;
    }

    public int hashCode() {
/* 71 */
        int hash = 0;
/* 72 */
        hash += (int) this.numeroSecuencia;
/* 73 */
        hash += this.codigoEmpresa;
/* 74 */
        hash += (this.referenciadocumento != null ? this.referenciadocumento.hashCode() : 0);
/* 75 */
        return hash;
    }

    public boolean equals(Object object) {
/* 81 */
        if (!(object instanceof DocumentoSriPK)) {
/* 82 */
            return false;
        }
/* 84 */
        DocumentoSriPK other = (DocumentoSriPK) object;
/* 85 */
        if (this.numeroSecuencia != other.numeroSecuencia) {
/* 86 */
            return false;
        }
/* 88 */
        if (this.codigoEmpresa != other.codigoEmpresa) {
/* 89 */
            return false;
        }
/* 91 */
        if (((this.referenciadocumento == null) && (other.referenciadocumento != null)) || ((this.referenciadocumento != null) && (!this.referenciadocumento.equals(other.referenciadocumento)))) {
/* 92 */
            return false;
        }
/* 94 */
        return true;
    }

    public String toString() {
/* 99 */
        return "emac.Entidades.BD.DocumentoSriPK[ numeroSecuencia=" + this.numeroSecuencia + ", codigoEmpresa=" + this.codigoEmpresa + ", referenciadocumento=" + this.referenciadocumento + " ]";
    }
}

/* Location:           /Users/rvallejo/Desktop/Fesri.XML-ejb.jar
 * Qualified Name:     emac.Entidades.BD.DocumentoSriPK
 * JD-Core Version:    0.6.2
 */