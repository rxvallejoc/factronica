/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.pos.jsf.data.model;

import com.obiectumclaro.factronica.core.model.Product;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author marco
 */
public class ProductDataModel extends ListDataModel<Product> implements SelectableDataModel<Product> {

    public ProductDataModel() {
    }

    public ProductDataModel(List<Product> list) {
        super(list);
    }

    @Override
    public Object getRowKey(Product t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @SuppressWarnings("unchecked")	
    public Product getRowData(String rowKey) {

        List<Product> products = (List<Product>) getWrappedData();

        for (Product product : products) {
            if (product.getCode().equals(rowKey)) {
                return product;
            }
        }

        return null;
    }
}
