/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author marco
 */
@Entity
public class AdditionaInformationCustomer implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long pk;
    private String value;
    private String description;

    public AdditionaInformationCustomer(final String value, final String description) {
		this.value = value;
		this.description = description;
	}

    /**
     * @return the pk
     */
    public Long getPk() {
        return pk;
    }

	/**
     * @param pk the pk to set
     */
    public void setPk(Long pk) {
        this.pk = pk;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
}
