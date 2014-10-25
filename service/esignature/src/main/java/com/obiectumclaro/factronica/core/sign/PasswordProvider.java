package com.obiectumclaro.factronica.core.sign;

import java.security.cert.X509Certificate;

import xades4j.providers.impl.KeyStoreKeyingDataProvider;


public class PasswordProvider implements KeyStoreKeyingDataProvider.KeyStorePasswordProvider, KeyStoreKeyingDataProvider.KeyEntryPasswordProvider {

    private final char[] password;

    public PasswordProvider(String password) {
        this.password = password.toCharArray();
    }

    @Override
    public char[] getPassword(String entryAlias, X509Certificate entryCert) {
        return password;
    }

    @Override
    public char[] getPassword() {
        return password;
    }

}
