package com.obiectumclaro.factronica.api.invoice.domain;


import java.math.BigDecimal;

public class TotalTax {
    private final String code;
    private final String percentageCode;
    private final BigDecimal baseAmount;
    private final BigDecimal amount;

    public TotalTax(String code, String percentageCode, BigDecimal baseAmount, BigDecimal amount) {
        this.code = code;
        this.percentageCode = percentageCode;
        this.baseAmount = baseAmount;
        this.amount = amount;

    }
}
