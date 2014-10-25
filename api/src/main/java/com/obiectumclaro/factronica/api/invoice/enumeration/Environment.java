package com.obiectumclaro.factronica.api.invoice.enumeration;


public enum Environment {
    PRODUCTION("2", "PRODUCCION"), TEST("1", "PRUEBAS");

    private String identificationCode;
    private String description;

    private Environment(String identificationCode, String description) {
        this.identificationCode = identificationCode;
        this.description = description;
    }
}
