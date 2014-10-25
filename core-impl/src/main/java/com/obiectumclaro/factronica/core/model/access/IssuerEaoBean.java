/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.model.access;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.obiectumclaro.factronica.core.model.Issuer;

/**
 *
 * @author marco zaragocin
 */
@Stateless
public class IssuerEaoBean extends BaseEaoBean{
    public Issuer getLogedIssuerById(Long id){
        StringBuilder sql = new StringBuilder("select p from Issuer p where p.pk=:id ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("id", id);
        return (Issuer) query.getSingleResult();
    }
}
