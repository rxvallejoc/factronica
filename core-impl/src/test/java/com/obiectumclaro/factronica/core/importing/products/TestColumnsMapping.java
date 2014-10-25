/**
 * 
 */
package com.obiectumclaro.factronica.core.importing.products;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.obiectumclaro.factronica.core.importing.products.ColumnsMapping;
import com.obiectumclaro.factronica.core.importing.products.HeaderLabel;

/**
 * @author iapazmino
 * 
 */
public class TestColumnsMapping {

	private static final HeaderLabel CODE = new HeaderLabel(2),
			ALT_CODE = new HeaderLabel(4), NAME = new HeaderLabel(6),
			DESCRIPTION = new HeaderLabel(8), TYPE = new HeaderLabel(0),
			PRICE = new HeaderLabel(1), TAX = new HeaderLabel(3);
	private static final HeaderLabel[] INDEXES = { CODE, ALT_CODE, NAME,
			DESCRIPTION, TYPE, PRICE, TAX };

	private ColumnsMapping mapping;

	@Before
	public void init() {
		mapping = new ColumnsMapping(INDEXES);
	}

	@Test
	public void shouldLoadFromArray() {
		final HeaderLabel codeIndex = mapping.getCode();
		final HeaderLabel altCodeIndex = mapping.getAltCode();
		final HeaderLabel nameIndex = mapping.getName();
		final HeaderLabel descIndex = mapping.getDescription();
		final HeaderLabel typeIndex = mapping.getType();
		final HeaderLabel priceIndex = mapping.getPrice();

		assertColumnsAreEquals(CODE, codeIndex);
		assertColumnsAreEquals(ALT_CODE, altCodeIndex);
		assertColumnsAreEquals(NAME, nameIndex);
		assertColumnsAreEquals(DESCRIPTION, descIndex);
		assertColumnsAreEquals(TYPE, typeIndex);
		assertColumnsAreEquals(PRICE, priceIndex);
	}

	private void assertColumnsAreEquals(final HeaderLabel expected,
			final HeaderLabel actual) {
		assertEquals("The expected index was " + expected + " but got "
				+ actual, expected, actual);
	}

}
