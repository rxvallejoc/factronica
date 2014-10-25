package com.obiectumclaro.factronica.core.importing.invoices.tab;

import java.io.Serializable;

public class ProductLine implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String code;
    private String amount;
    private String product;
    private String description;
    private String price;
    private String subTotal;

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getAmount() {
	return amount;
    }

    public void setAmount(String amount) {
	this.amount = amount;
    }

    public String getProduct() {
	return product;
    }

    public void setProduct(String product) {
	this.product = product;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getPrice() {
	return price;
    }

    public void setPrice(String price) {
	this.price = price;
    }

    public String getSubTotal() {
	return subTotal;
    }

    public void setSubTotal(String subTotal) {
	this.subTotal = subTotal;
    }

    @Override
    public String toString() {
	return "ProductFile [code=" + code + ", amount=" + amount + ", product=" + product + ", description="
		+ description + ", price=" + price + ", subTotal=" + subTotal + "]";
    }

}
