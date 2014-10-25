package com.obiectumclaro.factronica.api.invoice.domain;


public class Issuer {

    private final String name;
    private final String commercialName;
    private final String ruc;
    private final String mainAddress;
    private final boolean accountingRequired;
    private final String commercialVenue;
    private final String pointOfSale;
    private final String commercialVenueAddress;
    private final String specialTaxPayer;


    public Issuer(final String name, final String commercialName, final String ruc, final String mainAddress, final boolean accountingRequired, final String commercialVenue, final String pointOfSale, final String commercialVenueAddress, final String specialTaxPayer) {
        this.name = name;
        this.commercialName = commercialName;
        this.ruc = ruc;
        this.mainAddress = mainAddress;
        this.accountingRequired = accountingRequired;
        this.commercialVenue = commercialVenue;
        this.pointOfSale = pointOfSale;
        this.commercialVenueAddress = commercialVenueAddress;
        this.specialTaxPayer = specialTaxPayer;
    }


    public String getName() {
        return name;
    }

    public String getRuc() {
        return ruc;
    }

    public String getMainAddress() {
        return mainAddress;
    }

    public boolean isAccountingRequired() {
        return accountingRequired;
    }

    public String getCommercialName() {
        return commercialName;
    }

    public String getCommercialVenue() {
        return commercialVenue;
    }

    public String getPointOfSale() {
        return pointOfSale;
    }

    public String getCommercialVenueAddress() {
        return commercialVenueAddress;
    }

    public String getSpecialTaxPayer() {
        return specialTaxPayer;
    }
}
