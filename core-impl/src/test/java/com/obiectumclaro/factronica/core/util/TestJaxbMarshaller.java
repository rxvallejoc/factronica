/**
 * 
 */
package com.obiectumclaro.factronica.core.util;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.obiectumclaro.factronica.core.model.Invoice;
import com.obiectumclaro.factronica.core.model.InvoiceItem;
import com.obiectumclaro.factronica.core.model.Product;

/**
 * Verification tests for the {@link JaxbMarshaller}.
 * 
 * @author iapazmino
 * 
 */
public class TestJaxbMarshaller {

	private static final String ACCESS_KEY = "0123456789012345678901234567890123456789012345678";

	private JaxbMarshaller<Invoice> marshaller;
	private Invoice invoice;

	private String marshalledOutput;

	@Before
	public void marshallAndConvertToString() {
		marshaller = new JaxbMarshaller<>(Invoice.class);
		invoice = newInvoice();

		final byte[] invoiceBytes = marshaller.toByteArray(invoice);
		marshalledOutput = new String(invoiceBytes);
	}

	@Test
	@Ignore
	public void rootTagShouldBeFactura() {
		final String tagFactura = "<?xml version=\"1.0\" ?><factura>";
		assertTrue(marshalledOutput, marshalledOutput.startsWith(tagFactura));
	}

	@Test
	@Ignore
	public void shouldIncludeAccessKey() {
		final String accessKey = "<accesKey>" + ACCESS_KEY + "</accesKey>";
		assertTrue(marshalledOutput, marshalledOutput.contains(accessKey));
	}

	@Test
	@Ignore
	public void shouldIncludeProduct() {
		assertTrue(marshalledOutput, marshalledOutput.contains("<product>"));
	}

	private Invoice newInvoice() {
		final List<InvoiceItem> items = new ArrayList<InvoiceItem>();
                InvoiceItem invoiceItem=new InvoiceItem(new Product());
		items.add(invoiceItem);
		final Invoice invoice = new Invoice();
		invoice.setAccesKey(ACCESS_KEY);
		invoice.setItems(items);
		return invoice;
	}

	@SuppressWarnings("unused")
	private Product newProduct() {
		final Product product = new Product();
		product.setCode("123");
		product.setAmount(new BigDecimal("10.00"));
		//product.setIvaTax("12");
		return product;
	}

}
