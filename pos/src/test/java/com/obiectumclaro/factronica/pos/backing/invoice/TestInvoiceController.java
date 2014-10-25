/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.invoice;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.primefaces.component.api.UIData;

import com.obiectumclaro.factronica.core.model.Customer;
import com.obiectumclaro.factronica.core.model.InvoiceItem;
import com.obiectumclaro.factronica.core.model.Product;
import com.obiectumclaro.factronica.core.model.access.CustomerEaoBean;
import com.obiectumclaro.factronica.core.model.access.ProductEaoBean;
import com.obiectumclaro.factronica.core.service.IssuerBean;
import com.obiectumclaro.factronica.pos.backing.TestBackingBean;
import com.obiectumclaro.factronica.web.constant.Constant;

/**
 * @author iapazmino
 *
 */
public class TestInvoiceController extends TestBackingBean {

	private InvoiceController backing;
	private IssuerBean issuerService;
	
	@Before
	public void init() {
		backing = new InvoiceController();
		issuerService = mock(IssuerBean.class);
		backing.setIssuerBean(issuerService);
		backing.initData();
	}
	
	@Test
	public void shouldFindCustomer() {
		final CustomerEaoBean customerEao = mock(CustomerEaoBean.class);
		final Customer customer = new Customer(1L);
		backing.setSelectedCustomer(customer);
		backing.setCustomerEaoBean(customerEao);
		backing.searchCustomer(Constant.SEARCH_CUSTOMMER);
		assertEquals(new Customer(1L), backing.getCustomer());
	}

	@Ignore
	@Test
	public void shouldDefaultToOneNewItemsQuantity() {
		final UIData table = mock(UIData.class);
		when(table.getRowIndex()).thenReturn(0);
		final ProductEaoBean prodEao = mock(ProductEaoBean.class);
		final Product product = new Product("code01");
		when(prodEao.findByCode(anyString())).thenReturn(product);
		backing.setProductEao(prodEao);
		backing.setProductsTable(table);
		final InvoiceItem item = backing.getInvoice().getItems().get(0);
		assertEquals(BigDecimal.ONE, item.getProduct().getAmount());
	}
	
}
