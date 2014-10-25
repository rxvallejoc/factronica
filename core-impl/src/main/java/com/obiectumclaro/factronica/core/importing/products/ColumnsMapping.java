/**
 * 
 */
package com.obiectumclaro.factronica.core.importing.products;


/**
 * Holds the column indexes to use when importing products from a spreadsheet.
 * 
 * @author iapazmino
 *
 */
public class ColumnsMapping {

	private final HeaderLabel[] indexes;

	public ColumnsMapping(final HeaderLabel[] indexes) {
		this.indexes = indexes;
	}

	public HeaderLabel getCode() {
		return indexes[0];
	}

	public HeaderLabel getAltCode() {
		return indexes[1];
	}

	public HeaderLabel getName() {
		return indexes[2];
	}

	public HeaderLabel getDescription() {
		return indexes[3];
	}

	public HeaderLabel getType() {
		return indexes[4];
	}

	public HeaderLabel getPrice() {
		return indexes[5];
	}
	
}
