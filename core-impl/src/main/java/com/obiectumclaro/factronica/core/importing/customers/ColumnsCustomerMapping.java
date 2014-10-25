/**
 * 
 */
package com.obiectumclaro.factronica.core.importing.customers;

import com.obiectumclaro.factronica.core.importing.products.HeaderLabel;


/**
 * Holds the column indexes to use when importing products from a spreadsheet.
 * 
 * @author iapazmino
 *
 */
public class ColumnsCustomerMapping {

	private final HeaderLabel[] indexes;

	public ColumnsCustomerMapping(final HeaderLabel[] indexes) {
		this.indexes = indexes;
	}
	
	public HeaderLabel getId(){
		return indexes[0];
	}
	
	public HeaderLabel getIdType(){
		return indexes[1];
	}
	
	public HeaderLabel getEmail(){
		return indexes[2];
	}
	
	public HeaderLabel getPerson(){
		return indexes[3];
	}
	
	public HeaderLabel getSocialReason(){
		return indexes[4];
	}
	
	public HeaderLabel getName(){
		return indexes[5];
	}
	
	public HeaderLabel getLastName(){
		return indexes[6];
	}

	public HeaderLabel getAddress(){
		return indexes[7];
	}
	
	public HeaderLabel getPhone(){
		return indexes[8];
	}
}
