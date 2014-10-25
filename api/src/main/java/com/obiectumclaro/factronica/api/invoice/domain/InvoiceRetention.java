package com.obiectumclaro.factronica.api.invoice.domain;


import java.math.BigDecimal;

public class InvoiceRetention {

    private String code;
    private String percentageCode;
    private BigDecimal rate;
    private BigDecimal amount;

    public String getPercentageCode() {
        return percentageCode;
    }

    public void setPercentageCode(String percentageCode) {
        this.percentageCode = percentageCode;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
