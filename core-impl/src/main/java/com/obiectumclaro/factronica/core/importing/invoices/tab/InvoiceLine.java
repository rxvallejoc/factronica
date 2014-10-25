/**
 * 
 */
package com.obiectumclaro.factronica.core.importing.invoices.tab;

import java.io.Serializable;
import java.util.List;

import com.obiectumclaro.file.reader.descriptor.annotations.FCD;
import com.obiectumclaro.file.reader.descriptor.annotations.FCDGroup;
import com.obiectumclaro.file.reader.descriptor.annotations.FCDList;
import com.obiectumclaro.file.reader.descriptor.annotations.FD;

/**
 * @author rvallejo
 * 
 */
@FD(columnSeparator = "\t", name = "InvoiceFile", validator = InvoiceFileValidator.class)
public class InvoiceLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @FCD(position = 0)
    private String invoiceNumber;

    @FCD(position = 1)
    private String dateOfIssue;

    @FCD(position = 2)
    private String contractNumber;

    @FCD(position = 4)
    private String cusotmerLogin;

    @FCD(position = 5)
    private String name;

    @FCD(position = 6)
    private String id;

    @FCD(position = 8)
    private String address;

    @FCD(position = 9)
    private String phone;

    @FCD(position = 65)
    private String subTotal;

    @FCD(position = 67, optional = true)
    private String discount;

    @FCD(position = 68)
    private String subTotalAfeterDiscount;

    @FCD(position = 69)
    private String iva;

    @FCD(position = 70)
    private String total;

    @FCD(position = 73)
    private String paymentDeathline;

    @FCD(position = 77, optional = true)
    private String email;

    @FCDList(elementsClass = ProductLine.class, group = {
	    @FCDGroup(name = "code", positions = { 11, 17, 23, 29, 35, 41, 47, 53, 59 }, defaultValues = { "", "", "",
		    "", "", "", "", "", "" }, optional = true),
	    @FCDGroup(name = "amount", positions = { 12, 18, 24, 30, 36, 42, 48, 54, 60 }, defaultValues = { "", "",
		    "", "", "", "", "", "", "" }, optional = true),
	    @FCDGroup(name = "product", positions = { 13, 19, 25, 31, 37, 43, 49, 55, 61 }, defaultValues = { "", "",
		    "", "", "", "", "", "", "" }, optional = true),
	    @FCDGroup(name = "description", positions = { 14, 20, 26, 32, 38, 44, 50, 56, 62 }, defaultValues = { "",
		    "", "", "", "", "", "", "", "" }, optional = true),
	    @FCDGroup(name = "price", positions = { 15, 21, 27, 33, 39, 45, 51, 57, 63 }, defaultValues = { "", "", "",
		    "", "", "", "", "", "" }, optional = true),
	    @FCDGroup(name = "subTotal", positions = { 16, 22, 28, 34, 40, 46, 52, 58, 64 }, defaultValues = { "", "",
		    "", "", "", "", "", "", "" }, optional = true) })
    private List<ProductLine> producst;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getCusotmerLogin() {
	return cusotmerLogin;
    }

    public void setCusotmerLogin(String cusotmerLogin) {
	this.cusotmerLogin = cusotmerLogin;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public String getSubTotal() {
	return subTotal;
    }

    public void setSubTotal(String subTotal) {
	this.subTotal = subTotal;
    }

    public String getDiscount() {
	return discount;
    }

    public void setDiscount(String discount) {
	this.discount = discount;
    }

    public String getSubTotalAfeterDiscount() {
	return subTotalAfeterDiscount;
    }

    public void setSubTotalAfeterDiscount(String subTotalAfeterDiscount) {
	this.subTotalAfeterDiscount = subTotalAfeterDiscount;
    }

    public String getIva() {
	return iva;
    }

    public void setIva(String iva) {
	this.iva = iva;
    }

    public String getTotal() {
	return total;
    }

    public void setTotal(String total) {
	this.total = total;
    }

    public List<ProductLine> getProducst() {
	return producst;
    }

    public void setProducst(List<ProductLine> producst) {
	this.producst = producst;
    }

    public String getDateOfIssue() {
	return dateOfIssue;
    }

    public void setDateOfIssue(String dateOfIssue) {
	this.dateOfIssue = dateOfIssue;
    }

    public String getInvoiceNumber() {
	return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
	this.invoiceNumber = invoiceNumber;
    }

    public String getContractNumber() {
	return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
	this.contractNumber = contractNumber;
    }

    public String getPaymentDeathline() {
	return paymentDeathline;
    }

    public void setPaymentDeathline(String paymentDeathline) {
	this.paymentDeathline = paymentDeathline;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
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
}
