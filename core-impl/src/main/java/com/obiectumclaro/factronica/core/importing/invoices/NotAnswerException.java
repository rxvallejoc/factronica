/**
 * 
 */
package com.obiectumclaro.factronica.core.importing.invoices;

import com.obiectumclaro.factronica.core.web.service.sri.client.ServiceEnvironment;

/**
 * Exception for a request without response that should be queued again.
 * 
 * @author ipazmino
 * 
 */
public class NotAnswerException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String MESSAGE_PATTERN = "Fallo al intentar consultar la autorizacion de la clave %s en el ambiente %s. Se volvera a intentar.";

    /**
     * @param message
     */
    public NotAnswerException(final String accessKey, final ServiceEnvironment environment) {
	super(String.format(MESSAGE_PATTERN, accessKey, environment));
    }

    /**
     * @param message
     * @param cause
     */
    public NotAnswerException(final String accessKey, final ServiceEnvironment environment, final Throwable cause) {
	super(String.format(MESSAGE_PATTERN, accessKey, environment), cause);
    }

}
