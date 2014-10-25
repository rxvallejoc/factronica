/**
 * 
 */
package com.obiectumclaro.factronica.core.importing.invoices.tab;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author rvallejo
 *
 */
public class TABFileImporterTest {
	
	@Test
	public void ShouldReturnInvoicesParsedGivenAFile() throws IOException{
		TABFileImporter tabFileImporter= new TABFileImporter();
		List<InvoiceLine> invoices = tabFileImporter.readTABFile(toByteArray(getAsStream("archivoCourier.txt")));
		Assert.assertEquals(848, invoices.size());
	}
	
	private InputStream getAsStream(final String file) {
		final ClassLoader cl = Thread.currentThread().getContextClassLoader();
		return cl.getResourceAsStream(file);
	}
	
	public static byte[] toByteArray(InputStream is) throws IOException{ 
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		int reads = is.read(); 
		while(reads != -1){ 
			baos.write(reads); 
			reads = is.read(); 
			} 
		return baos.toByteArray(); 
	}
	
	

}
