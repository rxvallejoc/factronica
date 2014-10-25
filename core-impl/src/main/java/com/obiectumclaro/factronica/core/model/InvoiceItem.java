/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.obiectumclaro.factronica.core.enumeration.ProductType;

/**
 *
 * @author marco zaragocin
 */
@Entity
public class InvoiceItem implements Serializable {


    
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long pk;
    private String alternateCode;
    private String code;
    private String name;
    private ProductType productType;
    private String description;
    private BigDecimal price;
    @Transient
    private BigDecimal discount;
    @Transient
    private BigDecimal amount;
    @Transient
    private BigDecimal total;
    @Transient
    private Product product;
    public InvoiceItem() {
    }

    public InvoiceItem(Product product) {
        this.product=product;
        this.alternateCode=product.getAlternateCode();
        this.code=product.getCode();
        this.description=product.getDescription();
        this.discount=product.getDiscount();
        this.name= product.getName();
        this.price=product.getUnitPrice();
        this.productType=product.getProductType();
        this.amount=product.getAmount();
        this.total=product.getTotal();
        
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
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the alternateCode
     */
    public String getAlternateCode() {
        return alternateCode;
    }

    /**
     * @param alternateCode the alternateCode to set
     */
    public void setAlternateCode(String alternateCode) {
        this.alternateCode = alternateCode;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the productType
     */
    public ProductType getProductType() {
        return productType;
    }

    /**
     * @param productType the productType to set
     */
    public void setProductType(ProductType productType) {
        this.productType = productType;
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

    /**
     * @return the discount
     */
    public BigDecimal getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvoiceItem [alternateCode=").append(alternateCode)
				.append(", code=").append(code).append(", name=").append(name)
				.append(", productType=").append(productType)
				.append(", description=").append(description)
				.append(", price=").append(price).append(", discount=")
				.append(discount).append(", amount=").append(amount)
				.append(", total=").append(total).append("]");
		return builder.toString();
	}
}
