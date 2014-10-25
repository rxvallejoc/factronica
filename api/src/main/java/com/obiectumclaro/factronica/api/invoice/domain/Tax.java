package com.obiectumclaro.factronica.api.invoice.domain;

import java.math.BigDecimal;


public class Tax {
    private final String code;
    private final String percentageCode;
    private final BigDecimal rate;
    private final BigDecimal baseAmount;
    private final BigDecimal amount;

    public Tax(String code, String percentageCode, BigDecimal rate, BigDecimal baseAmount, BigDecimal amount) {
        this.code = code;
        this.percentageCode = percentageCode;
        this.rate = rate;
        this.baseAmount = baseAmount;
        this.amount = amount;

    }
}
