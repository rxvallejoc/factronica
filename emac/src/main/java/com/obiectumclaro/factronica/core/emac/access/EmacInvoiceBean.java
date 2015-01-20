package com.obiectumclaro.factronica.core.emac.access;

import com.obiectumclaro.factronica.core.emac.model.DocumentoSri;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class EmacInvoiceBean extends BaseEaoBean{

    public List<DocumentoSri> getDocumentsForRequest(){
        Query query = entityManager.createQuery("SELECT  d FROM DocumentoSri d WHERE estadoproceso = 'IN' ");
        return query.getResultList();
    }

    public DocumentoSri update(DocumentoSri sriDocument){
        return merge(sriDocument);
    }

}
