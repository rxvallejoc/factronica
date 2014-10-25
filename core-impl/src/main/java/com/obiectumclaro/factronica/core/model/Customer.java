/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.obiectumclaro.factronica.core.enumeration.IdType;
import com.obiectumclaro.factronica.core.enumeration.Person;

/**
 * 
 * @author maza261109
 */
@Entity
@Table(name = "Customer", uniqueConstraints = { @UniqueConstraint(columnNames = { "id" }) })
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Customer.findById", query = "SELECT c FROM Customer c WHERE c.id = ?"),
    @NamedQuery(name = "Customer.findByIdType", query = "select c from Customer c where c.idType = :idType and c.id = :id"),
    @NamedQuery(name = "Customer.findAll", query = "select c from Customer c") })
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue
    @Column(name = "pk")
    private Long pk;
    
    @Size(max = 255)
    @Column(name = "id")
    private String id;
    
    @Column(name = "id_type")
    @Enumerated(EnumType.STRING)
    private IdType idType;
    
    @Basic(optional = false)
    @NotNull
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    @Size(min = 1, max = 255)
    @Column(name = "email")
    private String email;
    
    @NotNull
    private Person person = Person.NATURAL;
    
    @Size(max = 255)
    @Column(name = "socialReason")
    private String socialReason;

    @Size(max = 255)
    @Column(name = "name")
    private String name;
    
    @Size(max = 255)
    @Column(name = "lastName")
    private String lastName;

    @Size(max = 255)
    @Column(name = "address")
    private String address;
    
    @Size(max = 255)
    @Column(name = "phone")
    private String phone;
    
    @JoinColumn(name = "agreement_pk", referencedColumnName = "pk")
    @ManyToOne(cascade = CascadeType.ALL)
    private Agreement agreement;
    
    @OneToMany
    private List<Invoice> invoices;

    public Customer() {
    }

    @Transient
    public String getLegalName() {
        if (Person.NATURAL.equals(person)) {
            return String.format("%s %s", name==null?"":name, lastName==null?"":lastName);
        } else {
            return socialReason;
        }
    }

    public Customer(Long pk) {
        this.pk = pk;
    }

    public Customer(Long pk, String email) {
        this.pk = pk;
        this.email = email;
    }

    public Customer(final IdType idType, String id, String name) {
        this.idType = idType;
        this.id = id;
        this.name = name;
    }

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IdType getIdType() {
        return idType;
    }

    public void setIdType(IdType idType) {
        this.idType = idType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Agreement getAgreement() {
        return agreement;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pk != null ? pk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.pk == null && other.pk != null) || (this.pk != null && !this.pk.equals(other.pk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.obiectumclaro.factronica.core.model.Customer[ pk=" + pk + " ]";
    }

    /**
     * @return the invoices
     */
    public List<Invoice> getInvoices() {
        return invoices;
    }

    /**
     * @param invoices
     *            the invoices to set
     */
    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     *            the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the socialReason
     */
    public String getSocialReason() {
        return socialReason;
    }

    /**
     * @param socialReason
     *            the socialReason to set
     */
    public void setSocialReason(String socialReason) {
        this.socialReason = socialReason;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

}
