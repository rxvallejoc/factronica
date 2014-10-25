/**
 * 
 */
package com.obiectumclaro.factronica.core.importing;

import com.obiectumclaro.factronica.core.importing.products.HeaderLabel;

/**
 * This is a exception to denote a failure while conversion of a value failed.
 * The message past to the super constructor will be formed using the header's
 * label and index in that specific order. <br />
 * <br />
 * Example message: Column Price in position 5 is not a number.
 * 
 * @author iapazmino
 * 
 */
public class ValueConversionException extends Exception {


	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an value conversion exception.
	 * 
	 * @param message
	 *            is actually a template where the header's label and index will
	 *            be replaced.
	 * @param header
	 *            is the column where the conversion failed.
	 */
	public ValueConversionException(final String message,
			final HeaderLabel header) {
		super(String.format(message, header.getLabel(), header.getIndex()));
	}

}
