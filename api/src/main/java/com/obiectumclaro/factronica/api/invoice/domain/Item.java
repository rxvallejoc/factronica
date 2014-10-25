package com.obiectumclaro.factronica.api.invoice.domain;


import java.math.BigDecimal;
import java.util.List;

public class Item {
    private final String code;
    private final String description;
    private final int quantity;
    private final BigDecimal unitPrice;
    private final BigDecimal dicount;
    private final BigDecimal totalPriceWithoutTax;
    private final List<Tax> taxes;

    public Item(String code, String description, int quantity, BigDecimal unitPrice, BigDecimal discount, BigDecimal totalPriceWithoutTax, List<Tax> taxes) {
        this.code = code;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.dicount = discount;
        this.totalPriceWithoutTax = totalPriceWithoutTax;
        this.taxes = taxes;
        
    }
}
