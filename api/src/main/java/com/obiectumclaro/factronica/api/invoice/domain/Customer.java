package com.obiectumclaro.factronica.api.invoice.domain;


import com.obiectumclaro.factronica.api.invoice.enumeration.IdentificationType;

public class Customer {

    private final String name;
    private final IdentificationType identificationType;
    private final String identification;

    public Customer(final String name, final IdentificationType identificationType, final String identification) {
        this.name = name;
        this.identificationType = identificationType;
        this.identification = identification;
    }

    public String getName() {
        return name;
    }

    public IdentificationType getIdentificationType() {
        return identificationType;
    }

    public String getIdentification() {
        return identification;
    }
}
