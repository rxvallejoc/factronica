/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.products;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.faces.component.html.HtmlInputText;
import javax.faces.event.ActionEvent;

import org.junit.Before;
import org.junit.Test;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.obiectumclaro.factronica.core.importing.products.HeaderLabel;
import com.obiectumclaro.factronica.core.importing.products.ProductsImporter;
import com.obiectumclaro.factronica.core.model.TaxValue;
import com.obiectumclaro.factronica.pos.backing.TestBackingBean;

/**
 * @author iapazmino
 *
 */
public class TestImportProductsBackingBean extends TestBackingBean {

	private static final byte[] BYTE_ARRAY = new byte[]{'t', 'e', 's', 't'};
	private static final String FILE_NAME = "test.xls";
	
	private ImportProductsBackingBean prodsImportBean;
	private ProductsImporter prodsImporter;
	
	@Before
	public void init() {
		prodsImporter = mock(ProductsImporter.class);
		
		prodsImportBean = new ImportProductsBackingBean();
		prodsImportBean.prodsImporter = prodsImporter;
		prodsImportBean.init();
	}
	
	@Test
	public void shouldEnableImportingOnFileUpload() throws IOException {
		final UploadedFile file = mock(UploadedFile.class);
		final FileUploadEvent evt = new FileUploadEvent(new HtmlInputText(), file);
		when(file.getInputstream()).thenReturn(new ByteArrayInputStream(BYTE_ARRAY));
		when(file.getFileName()).thenReturn(FILE_NAME);
		doNothing().when(prodsImporter).readSource(any(InputStream.class));
		
		prodsImportBean.handleFileUpload(evt);
		
		assertFalse(prodsImportBean.isDisableImport());
	}
	
	@Test
	public void shouldEnableDownloadReportOnFileImport() {
		prodsImportBean.setIndexes(new HeaderLabel[] {new HeaderLabel()});
		prodsImportBean.setTax(new TaxValue());
		
		prodsImportBean.startImportationListener(mock(ActionEvent.class));
		
		assertFalse(prodsImportBean.isDisableDownloadReport());
	}
	
	@Test
	public void shouldCreateReport() {
		prodsImportBean.xlsName = FILE_NAME;
		when(prodsImporter.getReport()).thenReturn(new ByteArrayInputStream(BYTE_ARRAY));
		
		prodsImportBean.downloadReportListener(mock(ActionEvent.class));
		
		final StreamedContent report = prodsImportBean.getReport();
		assertEquals("report-" + FILE_NAME, report.getName());
	}
	
}
