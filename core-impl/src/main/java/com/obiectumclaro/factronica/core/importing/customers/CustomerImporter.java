/**
 * 
 */
package com.obiectumclaro.factronica.core.importing.customers;

import java.io.InputStream;
import java.util.List;

import com.obiectumclaro.factronica.core.importing.products.HeaderLabel;
import com.obiectumclaro.factronica.core.model.TaxValue;

/**
 * Contract for importing customers from a file to the inventory.
 * 
 * @author iapazmino
 * 
 */
public interface CustomerImporter {

	/**
	 * Sets the spreadsheet to operate over.
	 * 
	 * @param source
	 *            the file where the customers to be imported are.
	 * @return
	 */
	void readSource(InputStream source);

	/**
	 * Reads the spread sheet's headers. First row in the file is considered to
	 * hold the headers for each column.
	 * 
	 * @return a list with the headers' names
	 */
	List<HeaderLabel> getHeaders();

	/**
	 * Actually imports the rows inside the source file into the storage.
	 * 
	 * @param mapping
	 *            keeps the columns' indexes
	 * @param tax
	 *            is the {@link TaxValue} to be applied to all the products
	 * @return
	 */
	void importCustomers(ColumnsCustomerMapping mapping);

	/**
	 * @return the source file with the imported products updated with the
	 *         processing result for each product.
	 */
	InputStream getReport();

}
