package com.obiectumclaro.factronica.core.service;

import java.util.List;

import com.obiectumclaro.factronica.core.model.Product;
import com.obiectumclaro.factronica.core.model.TaxValue;
import com.obiectumclaro.factronica.core.service.exception.ProductAdditionException;

public interface Products {

    /**
     * Creates a new product in Product's repository
     * 
     * @param product
     *            to create
     * @param taxes
     *            associated with product
     * @throws ProductAdditionException
     */
    void addProduct(Product product, List<TaxValue> taxes) throws ProductAdditionException;

    void updateProduct(Product product, List<TaxValue> taxes) throws ProductAdditionException;

    /**
     * Obtains all products
     * 
     * @return list of products
     */
    List<Product> findAllProducts();

    Product findByPk(Long productId);

    TaxValue findTaxValueById(long id);

    List<TaxValue> listTaxValues();

    public Product getProductByCode(String code);

}