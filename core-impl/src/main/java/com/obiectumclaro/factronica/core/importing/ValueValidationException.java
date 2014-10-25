/**
 * 
 */
package com.obiectumclaro.factronica.core.importing;

import com.obiectumclaro.factronica.core.importing.products.HeaderLabel;

/**
 * This is a exception to denote validation failed. The message past to the
 * super constructor will be formed using the header's label and index in that
 * specific order. <br />
 * <br />
 * Example message: Column Price in position 5 can't be lesser than 0.
 * 
 * @author iapazmino
 * 
 */
public class ValueValidationException extends Exception {
	

	private static final long serialVersionUID = 1L;
	private String rawMessage;
	private HeaderLabel header;

	public ValueValidationException(final String rawMessage) {
		super(rawMessage);
		this.header = new HeaderLabel();
	}
	
	public ValueValidationException(final String rawMessage,
			final HeaderLabel header) {
		super(String.format(rawMessage, header.getLabel(), header.getIndex()));
		this.rawMessage = rawMessage;
		this.header = header;
	}

	public String getRawMessage() {
		return rawMessage;
	}

	public HeaderLabel getHeader() {
		return header;
	}
	
}
