/**
 * 
 */
package com.obiectumclaro.factronica.core.importing.invoices;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.obiectumclaro.factronica.core.importing.invoices.csv.InvoiceLine;

/**
 * Verification on the validation of every field when building a invoice line.
 * 
 * @author ipazmino
 * 
 */
@RunWith(value = Parameterized.class)
public class TestInvoiceLineValidations {

	private InvoiceLine line;
	private String[] tokens;
	private String error;

	public TestInvoiceLineValidations(final String[] tokens, final String error) {
		this.tokens = tokens;
		this.error = error;
	}

	@Parameters
	public static Collection<Object[]> examples() {
		final Object[][] examples = new Object[][] { 
				{ new String[] { "tipo id", "id", "producto" }, "Cada fila debe tener al menos 5 columnas" },
				{ new String[] { "", "id", "fecha", "cantidad", "codigo" }, "El tipo de identificacion no puede estar nulo o vacio" },
				{ new String[] { "LICENCIA", "id", "fecha", "cantidad", "codigo" }, "Los tipos de identificacion validos son CEDULA, RUC, PASAPORTE" },
				{ new String[] { "CEDULA", "", "fecha", "cantidad", "codigo" }, "El numero de identificacion no puede estar nulo o vacio" },
				{ new String[] { "CEDULA", "1712345678", "", "cantidad", "codigo" }, "La fecha de emision no puede estar nulo o vacio" },
				{ new String[] { "CEDULA", "1712345678", "30-10-2013", "cantidad", "codigo" }, "La fecha debe seguir el patron dd/MM/yyyy" },
				{ new String[] { "CEDULA", "1712345678", "30/10/2013", "", "codigo" }, "La cantidad de productos no puede estar nulo o vacio" },
				{ new String[] { "CEDULA", "1712345678", "30/10/2013", "A", "codigo" }, "Se esperaba un numero para la cantidad de productos pero se obtuvo [A]" },
				{ new String[] { "CEDULA", "1712345678", "30/10/2013", "1", "" }, "El codigo del producto no puede estar nulo o vacio" },
				{ new String[] { "CEDULA", "1712345678", "30/10/2013", "1", "COD01","Contrato:123" }, "La informacion adicional debe tener el formato nombre=valor" },
				{ new String[] { "CEDULA", "1712345678", "30/10/2013", "1", "COD01","Contrato=123", "Fecha pago 30/10/2013" }, "La informacion adicional debe tener el formato nombre=valor" }
		};
		return Arrays.asList(examples);
	}

	@Test
	public void shouldValidateWhenBuildingAnInvoiceLine() {
		line = InvoiceLine.buildLine(tokens);
		assertEquals(error, line.getMessages().get(0));
	}
}
