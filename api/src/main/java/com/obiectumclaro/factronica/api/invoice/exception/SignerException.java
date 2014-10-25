/**
 * 
 */
package com.obiectumclaro.factronica.api.invoice.exception;


/**
 * Exception which denotes a problem while the document is to be signed.
 * 
 * @author @author <a href="mailto:rxvallejo@obiectumclaro.com">Rodrigo Vallejo</a>
 * 
 */
public class SignerException extends Exception {

	private static final long serialVersionUID = 1L;

	public SignerException(final String message) {
		super(message);
	}
	
	public SignerException(Throwable t){
        super(t);
    }
    
    public SignerException(String message, Throwable t){
        super(message, t);
    }


}
