/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.model;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author maza261109
 */
@Entity
@Table(name = "Invoice")
@XmlRootElement(name = "factura")
@NamedQueries({
    @NamedQuery(name = "Invoice.findAll", query = "SELECT i FROM Invoice i"),
    @NamedQuery(name = "Invoice.findByPk", query = "SELECT i FROM Invoice i WHERE i.pk = :pk"),
    @NamedQuery(name = "Invoice.findByAccesKey", query = "SELECT i FROM Invoice i WHERE i.accesKey = :accesKey")})
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long pk;
    private String accesKey;
    private BigDecimal tip = BigDecimal.ZERO;
    private Date issued = Calendar.getInstance().getTime();
    private String mainAddress;
    private String customerIdTypePrueba;
    private String customerIdNumber;
    private String guiaRemision;
    private String customerName;
    private String currency = "Dolar";
    private Issuer issuer;
    private List<InvoiceItem> items;
    private String xml;
    private String authorizationXml;
    private String authorizationNumber;
    @Temporal(TemporalType.DATE)
    private Date authorizationDate;
    private File file;
    @JoinColumn(name = "customer_pk", insertable = false, updatable = false)
    @ManyToOne
    private Customer customer;
    @Column(name = "customer_pk")
    private Long customerId;
    private List<AdditionaInformationCustomer> additionalInfoList;

    public Invoice() {
        items = new ArrayList<InvoiceItem>();
        additionalInfoList = new ArrayList<AdditionaInformationCustomer>();
    }

    public Invoice(Long pk) {
        this.pk = pk;
    }
    
    public void addAdditionalInfo(final AdditionaInformationCustomer additionalInfo) {
    	additionalInfoList.add(additionalInfo);
    }

    public void addAdditionalInfo() {
    	additionalInfoList.add(new AdditionaInformationCustomer(null, null));
    }
    
    @Id
    @GeneratedValue
    @Basic(optional = false)
    @Column(name = "pk")
    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    @Size(max = 255)
    @Column(name = "accesKey")
    @XmlElement(name = "claveAcceso")
    public String getAccesKey() {
        return accesKey;
    }

    public void setAccesKey(String accesKey) {
        this.accesKey = accesKey;
    }

    /**
     * @return the items
     */
    @OneToMany
    public List<InvoiceItem> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }

    public void addItem(final InvoiceItem item) {
    	this.items.add(item);
    }
    
    @Transient
    public List<TotalizedTax> getTotalizedTaxes() {
        final TotalizedTaxBuilder builder = new TotalizedTaxBuilder();
        return builder.getTotalizedTaxes(items);
    }

    @Transient
    public BigDecimal getTotalExcludedTaxes() {
        final TotalExcludedTaxesBuilder builder = new TotalExcludedTaxesBuilder();
        return builder.getTotalExcludedTaxes(items);
    }

    @Transient
    public BigDecimal getTotalDiscount() {
        final TotalDiscountBuilder builder = new TotalDiscountBuilder();
        if (items != null) {
            return builder.getTotalDiscount(items);
        }
        return BigDecimal.ZERO;

    }

    @Transient
    public BigDecimal getTotalDue() {
        final TotalDueBuilder builder = new TotalDueBuilder();
        return builder.getTotalDue(this);
    }

    @Transient
    public Issuer getIssuer() {
        return issuer;
    }

    /**
     * @return the tip
     */
    @Column(name = "tip")
    public BigDecimal getTip() {
        return tip;
    }

    /**
     * @param tip the tip to set
     */
    public void setTip(BigDecimal tip) {
        this.tip = tip;
    }

    @Column(name = "issued")
    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getIssued() {
        return issued;
    }

    public void setIssued(Date issued) {
        this.issued = issued;
    }

    @Column(name = "main_address")
    public String getMainAddress() {
        return mainAddress;
    }

    public void setMainAddress(String mainAddress) {
        this.mainAddress = mainAddress;
    }

    @Column(name = "customer_id_type_prueba")
    public String getCustomerIdTypePrueba() {
        return customerIdTypePrueba;
    }

    public void setCustomerIdTypePrueba(String customerIdType) {
        this.customerIdTypePrueba = customerIdType;
    }

    @Column(name = "customer_id_number")
    public String getCustomerIdNumber() {
        return customerIdNumber;
    }

    public void setCustomerIdNumber(String customerIdNumber) {
        this.customerIdNumber = customerIdNumber;
    }

    @Column(name = "guia_remision")
    public String getGuiaRemision() {
        return guiaRemision;
    }

    public void setGuiaRemision(String guiaRemision) {
        this.guiaRemision = guiaRemision;
    }

    @Column(name = "customer_name")
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Column(name = "currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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
        if (!(object instanceof Invoice)) {
            return false;
        }
        Invoice other = (Invoice) object;
        if ((this.pk == null && other.pk != null) || (this.pk != null && !this.pk.equals(other.pk))) {
            return false;
        }
        return true;
    }

    @Column
    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    /**
     * @return the file
     */
    @Transient
    public File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }

    public String getAuthorizationXml() {
        return authorizationXml;
    }

    public void setAuthorizationXml(String authorizationXml) {
        this.authorizationXml = authorizationXml;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the authorizationNumber
     */
    public String getAuthorizationNumber() {
        return authorizationNumber;
    }

    /**
     * @param authorizationNumber the authorizationNumber to set
     */
    public void setAuthorizationNumber(String authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }

    /**
     * @return the authorizationDate
     */
    public Date getAuthorizationDate() {
        return authorizationDate;
    }

    /**
     * @param authorizationDate the authorizationDate to set
     */
    public void setAuthorizationDate(Date authorizationDate) {
        this.authorizationDate = authorizationDate;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<AdditionaInformationCustomer> getAdditionalInfoList() {
		return additionalInfoList;
	}

	public void setAdditionalInfoList(
			List<AdditionaInformationCustomer> additionalInfoList) {
		this.additionalInfoList = additionalInfoList;
	}
}
