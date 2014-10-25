/**
 * 
 */
package com.obiectumclaro.factronica.core.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.obiectumclaro.factronica.core.model.Product;
import com.obiectumclaro.factronica.core.model.TaxValue;
import com.obiectumclaro.factronica.core.model.access.AdditionalProductInformationEaoBean;
import com.obiectumclaro.factronica.core.model.access.ProductEaoBean;
import com.obiectumclaro.factronica.core.model.access.TaxValueEaoBean;
import com.obiectumclaro.factronica.core.service.exception.ProductAdditionException;

/**
 * @author faustodelatog
 * 
 */
@Stateless
public class ProductsBean implements Products {

	@EJB
	private ProductEaoBean productEao;
	@EJB
	private TaxValueEaoBean taxValueEao;
	@EJB
	private AdditionalProductInformationEaoBean additionalProductInformationEao;

	/* (non-Javadoc)
	 * @see com.obiectumclaro.factronica.core.service.ProductsService#addProduct(com.obiectumclaro.factronica.core.model.Product, java.util.List)
	 */
	@Override
	public void addProduct(final Product product, final List<TaxValue> taxes) throws ProductAdditionException {
		if (null == taxes || taxes.isEmpty()) {
			throw new ProductAdditionException("El producto debe estar asociado al menos a un impuesto");
		}
		Product productAdded = productEao.findByCode(product.getCode());
		if (productAdded == null) {
			for (TaxValue taxValue : taxes) {
				product.addTaxValue(taxValueEao.findById(taxValue.getPk()));
			}
			productEao.save(product);
		} else {
			throw new ProductAdditionException(String.format("Ya existe un producto con c\u00f3digo %s registrado",
					product.getCode()));
		}
	}

	/* (non-Javadoc)
	 * @see com.obiectumclaro.factronica.core.service.ProductsService#updateProduct(com.obiectumclaro.factronica.core.model.Product, java.util.List)
	 */
	@Override
	public void updateProduct(final Product product, final List<TaxValue> taxes) throws ProductAdditionException {
		if (taxes.isEmpty()) {
			throw new ProductAdditionException(
					"El producto debe estar asociado al menos a un impuesto");
		}
		product.setTaxValueList(new ArrayList<TaxValue>());
		for (TaxValue taxValue : taxes) {
			
				product.addTaxValue(taxValueEao.findById(taxValue.getPk()));
			
		}
		productEao.update(product);
		
		
		
	}

	/* (non-Javadoc)
	 * @see com.obiectumclaro.factronica.core.service.ProductsService#findAllProducts()
	 */
	@Override
	public List<Product> findAllProducts() {
		return productEao.findAll();
	}

	/* (non-Javadoc)
	 * @see com.obiectumclaro.factronica.core.service.ProductsService#findByPk(java.lang.Long)
	 */
	@Override
	public Product findByPk(Long productId) {
		Product p = productEao.findByPk(productId);
		p.getAdditionalProductInformationList().size();
		return p;
	}
	
	@Override
	public List<TaxValue> listTaxValues() {
		return taxValueEao.findAll();
	}

	public TaxValue findTaxValueById(final long id) {
		return taxValueEao.findById(id);
	}

	public void setProductEao(ProductEaoBean productEao) {
		this.productEao = productEao;
	}

	public void setTaxValueEao(TaxValueEaoBean taxValueEao) {
		this.taxValueEao = taxValueEao;
	}
        
	public Product getProductByCode(String code){
            return this.productEao.findByCode(code);
        }
}
