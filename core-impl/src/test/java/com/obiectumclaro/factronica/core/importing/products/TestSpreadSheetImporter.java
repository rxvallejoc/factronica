/**
 * 
 */
package com.obiectumclaro.factronica.core.importing.products;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.obiectumclaro.factronica.core.importing.products.ColumnsMapping;
import com.obiectumclaro.factronica.core.importing.products.HeaderLabel;
import com.obiectumclaro.factronica.core.importing.products.SpreadSheetImporter;
import com.obiectumclaro.factronica.core.model.MockProductsFactory;
import com.obiectumclaro.factronica.core.model.TaxValue;
import com.obiectumclaro.factronica.core.service.Products;

/**
 * @author iapazmino
 * 
 */
public class TestSpreadSheetImporter {

	private InputStream xlsFile, inventory;

	private SpreadSheetImporter importer;
	private Products prodsService;

	@Before
	public void init() {
		xlsFile = getAsStream("simple.xls");
		inventory = getAsStream("productos.xls");
		importer = new SpreadSheetImporter();
		prodsService = mock(Products.class);
		importer.prods = prodsService;
	}

	@After
	public void closeStream() throws IOException {
		xlsFile.close();
		inventory.close();
	}

	@Test
	public void shouldReadHeaders() {
		final HeaderLabel[] expected = { new HeaderLabel("Column A"),
				new HeaderLabel("Column B"), new HeaderLabel("Column C") };

		importer.readSource(xlsFile);
		final List<HeaderLabel> result = importer.getHeaders();

		assertThat(result, hasItems(expected));
	}

	@Test
	public void shouldGetHeadersIndexes() {
		importer.readSource(xlsFile);
		final List<HeaderLabel> result = importer.getHeaders();

		assertColumnIsInRightIndex(result, "Column A", 0);
		assertColumnIsInRightIndex(result, "Column B", 1);
		assertColumnIsInRightIndex(result, "Column C", 2);
	}

	@Test
	public void shouldCountTheHeaders() {
		importer.readSource(inventory);
		final List<HeaderLabel> result = importer.getHeaders();

		final Integer length = result.size();
		assertThat(length, is(Integer.valueOf(7)));
	}

	@Test
	public void shouldAddResultsColumn() throws IOException {
		importer.readSource(inventory);
		final HeaderLabel[] indexes = { new HeaderLabel("Código", 0),
				new HeaderLabel("Código Alternativo", 1),
				new HeaderLabel("Nombre", 2),
				new HeaderLabel("Descripción", 3),
				new HeaderLabel("Tipo de Producto", 4),
				new HeaderLabel("Precio Unitario", 5) };
		final TaxValue iva = MockProductsFactory.createIva();
		
		importer.importProducts(new ColumnsMapping(indexes), iva);
		
		final InputStream report = importer.getReport();
		final Sheet sheet = new HSSFWorkbook(report).getSheetAt(0);
		final Row headersRow = sheet.getRow(0);
		final int lastHeader = headersRow.getLastCellNum() - 1;
		final Cell result = headersRow.getCell(lastHeader);
		assertEquals("Resultado", result.getStringCellValue());
		report.close();
	}

	private void assertColumnIsInRightIndex(final List<HeaderLabel> result,
			final String label, final int index) {
		final String message = "<%s> was not in the expected column <%s>";
		assertTrue(String.format(message, label, index),
				new HeaderLabel(index).equals(result.get(index)));
	}

	private InputStream getAsStream(final String file) {
		final ClassLoader cl = Thread.currentThread().getContextClassLoader();
		return cl.getResourceAsStream(file);
	}

}
