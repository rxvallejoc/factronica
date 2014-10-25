/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.products;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;

import org.primefaces.model.DualListModel;

import com.obiectumclaro.factronica.core.enumeration.ProductType;
import com.obiectumclaro.factronica.core.model.AdditionalProductInformation;
import com.obiectumclaro.factronica.core.model.Product;
import com.obiectumclaro.factronica.core.model.TaxValue;
import com.obiectumclaro.factronica.core.service.Products;
import com.obiectumclaro.factronica.core.service.TaxValuesBean;
import com.obiectumclaro.factronica.core.service.exception.ProductAdditionException;
import com.obiectumclaro.factronica.pos.jsf.FacesMessageHelper;

/**
 * @author faustodelatog
 * 
 */
@ManagedBean
@ViewScoped
public class ManageProductBackingBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Servicios
	@Inject
	private Products productsService;
	@Inject
	private TaxValuesBean taxesService;
	
	private String productId;
	private boolean requestedBefore;

	private Product newProduct;
	private DualListModel<TaxValue> taxes;
	private List<TaxValue> source;
	private List<TaxValue> target;
	
	public void searchForPruduct(final ComponentSystemEvent sysEvent) {
		if (requestedBefore) {
			return;
		}
		source = taxesService.findAllTaxes();
		if (null == productId || productId.isEmpty()) {
			newProduct = new Product();
			target = new ArrayList<>();
		} else {
			newProduct = productsService.findByPk(Long.valueOf(productId));
			target = new ArrayList<>(newProduct.getTaxValueList());
			source.removeAll(target);
		}
		taxes = new DualListModel<>(source, target);
		requestedBefore = true;
	}

	public void addAdditionalInfo() {
		newProduct.getAdditionalProductInformationList().add(new AdditionalProductInformation(newProduct));
	}

	public void removeAdditionalInfo(AdditionalProductInformation ai) {
		newProduct.getAdditionalProductInformationList().remove(ai);
	}

	public String addProduct() {
		String navRule = null;
		try {
			productsService.addProduct(newProduct, taxes.getTarget());
			navRule = "/products/list?faces-redirect=true";
			FacesMessageHelper.addFacesMessage(FacesMessage.SEVERITY_INFO, "El producto ha sido agregado", null);
		} catch (ProductAdditionException e) {
			FacesMessageHelper.addFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
		}

		return navRule;
	}

	public String updateProduct() {
		String navRule = null;
		try {
			productsService.updateProduct(newProduct, taxes.getTarget());
			navRule = "/products/list?faces-redirect=true";
			FacesMessageHelper.addFacesMessage(FacesMessage.SEVERITY_INFO, "El producto ha sido modificado", null);
		} catch (ProductAdditionException e) {
			FacesMessageHelper.addFacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
		}

		return navRule;
	}
	
	public ProductType[] getProductTypes() {
		return ProductType.values();
	}

	public Product getNewProduct() {
		return newProduct;
	}

	public DualListModel<TaxValue> getTaxes() {
		return taxes;
	}

	public void setTaxes(DualListModel<TaxValue> taxes) {
		this.taxes = taxes;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

}
