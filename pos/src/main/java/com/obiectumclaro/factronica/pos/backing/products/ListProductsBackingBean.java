/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.products;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.obiectumclaro.factronica.core.model.Product;
import com.obiectumclaro.factronica.core.service.Products;
import com.obiectumclaro.factronica.pos.backing.BaseBackingBean;

/**
 * @author faustodelatog
 * 
 */
@Named
public class ListProductsBackingBean extends BaseBackingBean {

	// Servicios
	@Inject
	private Products productsService;

	private List<Product> products;

	@PostConstruct
	public void init() {
		products = productsService.findAllProducts();
	}

	public String goToEdit(final Long productId) {
		String navRule = String.format("/products/product?productId=%sfaces-redirect=true", productId);
		return navRule;
	}

	public List<Product> getProducts() {
		return products;
	}

}
