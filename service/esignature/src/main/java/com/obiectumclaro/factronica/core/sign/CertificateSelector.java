package com.obiectumclaro.factronica.core.sign;

import java.security.cert.X509Certificate;
import java.util.List;

import xades4j.providers.impl.KeyStoreKeyingDataProvider;


public class CertificateSelector implements KeyStoreKeyingDataProvider.SigningCertSelector {

    @Override
    public X509Certificate selectCertificate(List<X509Certificate> availableCertificates) {
        return availableCertificates.get(0);
    }

}
