/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.service;

import com.obiectumclaro.factronica.core.model.Issuer;
import com.obiectumclaro.factronica.core.model.access.IssuerEaoBean;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author marco
 */
@Stateless
public class IssuerBean {
    @EJB
    IssuerEaoBean issuerEaoBean;
    
    public Issuer getIssuerById(Long id){
        Issuer issuer=issuerEaoBean.getLogedIssuerById(id);
        System.out.println("---ISSUER-->"+issuer);
        return issuer;
    }
}
