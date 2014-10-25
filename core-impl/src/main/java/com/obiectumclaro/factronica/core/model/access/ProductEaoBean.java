/**
 * 
 */
package com.obiectumclaro.factronica.core.model.access;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.obiectumclaro.factronica.core.model.Product;
import com.obiectumclaro.factronica.core.model.Product_;

/**
 * Access to {@link Product} entity for its manipulation.
 * 
 * @author faustodelatog
 * 
 */
@Stateless
public class ProductEaoBean extends BaseEaoBean {

	/**
	 * Persists a new product
	 * 
	 * @param product
	 */
	public void save(final Product product) {
		System.out.println("persisting " + product);
		entityManager.persist(product);
	}

	/**
	 * Obtains all products
	 * 
	 * @return list of products
	 */
	public List<Product> findAll() {
		return getResultListFromNamedQuery(Product.class, "Product.findAll");
	}

	/**
	 * Find a product by code
	 * 
	 * @param code
	 * @return a product
	 */
	public Product findByCode(String code) {
		Product product = getSingleResultFromNamedQuery(Product.class, "Product.findByCode", code);
		if (product != null) {
			product.getTaxValueList().size();
		}
		return product;
	}

	@SuppressWarnings("unchecked")
	public List<Product> findByCodeName(String code, String name) {
		StringBuilder sql = new StringBuilder("select p from Product p where 1=1");
		if (code != null && !code.isEmpty()) {
			sql.append(" and p.code like :code");
		}
		if (name != null && !name.isEmpty()) {
			sql.append(" and p.name like :name");
		}
		Query query = entityManager.createQuery(sql.toString());
		if (code != null && !code.isEmpty()) {
			query.setParameter("code", code);
		}
		if (name != null && !name.isEmpty()) {
			query.setParameter("name", name);
		}
		return query.getResultList();
	}
	
	public List<Product> findProducts(final String queryProducto){
	    CriteriaBuilder builder= entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query=builder.createQuery(Product.class);
        Root<Product> customer=query.from(Product.class);
        query.select(customer);
        List<Predicate> predicates=new ArrayList<Predicate>();
        predicates.add(builder.like(customer.get(Product_.code), "%"+queryProducto+"%"));
        predicates.add(builder.like(customer.get(Product_.name), "%"+queryProducto+"%"));
        
        Predicate orPredicate=builder.or(predicates.toArray(new Predicate[]{}));
        query.where(orPredicate);
        TypedQuery<Product> typedQuery=entityManager.createQuery(query);
        return typedQuery.getResultList();
	    
	}

	public Product findByPk(Long pk) {
		return getSingleResultFromNamedQuery(Product.class, "Product.findByPk", pk);
	}

	public void update(Product product) {
		entityManager.merge(product);

	}

}