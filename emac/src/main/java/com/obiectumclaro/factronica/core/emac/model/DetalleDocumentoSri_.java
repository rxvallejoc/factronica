package com.obiectumclaro.factronica.core.emac.model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;

@StaticMetamodel(DetalleDocumentoSri.class)
public class DetalleDocumentoSri_ {
    public static volatile SingularAttribute<DetalleDocumentoSri, BigDecimal> precioUnitario;
    public static volatile SingularAttribute<DetalleDocumentoSri, BigDecimal> descuento;
    public static volatile SingularAttribute<DetalleDocumentoSri, String> codAuxuliar;
    public static volatile SingularAttribute<DetalleDocumentoSri, BigDecimal> cantidad;
    public static volatile SingularAttribute<DetalleDocumentoSri, BigDecimal> precioTotalSnImpuesto;
    public static volatile SingularAttribute<DetalleDocumentoSri, String> descripcion;
    public static volatile SingularAttribute<DetalleDocumentoSri, String> codPrincipal;
    public static volatile SingularAttribute<DetalleDocumentoSri, DetalleDocumentoSriPK> detalleDocumentoSriPK;
    public static volatile SingularAttribute<DetalleDocumentoSri, BigDecimal> valorIva;
    public static volatile SingularAttribute<DetalleDocumentoSri, DocumentoSri> documentoSri;
}
