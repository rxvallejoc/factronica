/**
 * 
 */
package com.obiectumclaro.factronica.core.service;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.obiectumclaro.factronica.core.model.MockProductsFactory;
import com.obiectumclaro.factronica.core.model.Product;
import com.obiectumclaro.factronica.core.model.TaxValue;
import com.obiectumclaro.factronica.core.model.access.ProductEaoBean;
import com.obiectumclaro.factronica.core.model.access.TaxValueEaoBean;
import com.obiectumclaro.factronica.core.service.exception.ProductAdditionException;

/**
 * Test cases to verify {@link ProductsBean}
 * 
 * @author faustodelatog
 * 
 */
public class TestProductsBean {

	private static Product product;

	@BeforeClass
	public static void initProduct() {
		product = new Product();
		product.setCode("A001");
		product.setName("Borradores");
		product.setDescription("Borradores de tinta");
	}

	private ProductEaoBean productEao;
	private TaxValueEaoBean taxEao;
	private Products productsService;
	private List<TaxValue> taxes;

	@Before
	public void setup() {
		productsService = new ProductsBean();
		productEao = mock(ProductEaoBean.class);
		taxEao = mock(TaxValueEaoBean.class);
		((ProductsBean) productsService).setProductEao(productEao);
		((ProductsBean) productsService).setTaxValueEao(taxEao);
		taxes = new ArrayList<>();
	}

	@Test
	public void testAddNewUser() {
		taxes.add(MockProductsFactory.createIva());
		when(productEao.findByCode(product.getCode())).thenReturn(null);
		when(taxEao.findById(anyLong())).thenReturn(MockProductsFactory.createIva());
		try {
			productsService.addProduct(product, taxes);
		} catch (ProductAdditionException e) {
			fail(e.getMessage());
		}
	}

	@Test(expected = ProductAdditionException.class)
	public void testNotAddNewUser() throws ProductAdditionException {
		when(productEao.findByCode(product.getCode())).thenReturn(product);
		productsService.addProduct(product, taxes);
	}
}
