package com.obiectumclaro.factronica.api.invoice.enumeration;

public enum IdentificationType {

    RUC("04"), CEDULA("05"), PASAPORTE("06"), CONSUMIDOR_FINAL("07");

    private final String identificationCode;

    private IdentificationType(final String identificationCode) {
        this.identificationCode = identificationCode;
    }

    public static IdentificationType parse(final String identificationCode) {
        switch (identificationCode) {
            case "04":
                return RUC;
            case "05":
                return RUC;
            case "06":
                return RUC;
            case "07":
                return RUC;
            default:
                final String typeNotFoundMessage = String.format("%s does not match any type of identification", identificationCode);
                throw new IllegalArgumentException(typeNotFoundMessage);
        }
    }

    public String getIdentificationCode() {
        return identificationCode;
    }

}
