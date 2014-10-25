/**
 * 
 */
package com.obiectumclaro.factronica.core.model.access;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.obiectumclaro.factronica.core.enumeration.IdType;
import com.obiectumclaro.factronica.core.model.Customer;
import com.obiectumclaro.factronica.core.model.Customer_;
import com.obiectumclaro.factronica.core.model.access.exception.NoEntityException;

/**
 * Access to {@link Customer} entity for its manipulation.
 * 
 * @author faustodelatog
 * 
 */
@Stateless
public class CustomerEaoBean extends BaseEaoBean {

	/**
	 * Persists a new product
	 * 
	 * @param product
	 */
	public void save(final Customer customer) {
		entityManager.persist(customer);
	}

	public Customer findById(String id) {
		return getSingleResultFromNamedQuery(Customer.class, "Customer.findById", id);
	}
	

	public Customer findByIdType(final IdType idType, final String id) {
		try {
			final TypedQuery<Customer> findByType = entityManager.createNamedQuery("Customer.findByIdType", Customer.class);
			findByType.setParameter("idType", idType);
			findByType.setParameter("id", id);
			return findByType.getSingleResult();
		} catch (NoResultException e) {
			throw new NoEntityException(e);
		}
	}
	/**
	 * Find by name 
	 * @param queryName
	 * @return
	 */
	public List<Customer> findByName(final String queryName){
		CriteriaBuilder builder= entityManager.getCriteriaBuilder();
		CriteriaQuery<Customer> query=builder.createQuery(Customer.class);
		Root<Customer> customer=query.from(Customer.class);
		query.select(customer);
		List<Predicate> predicates=new ArrayList<Predicate>();
		predicates.add(builder.like(customer.get(Customer_.name), "%"+queryName+"%"));
		predicates.add(builder.like(customer.get(Customer_.lastName), "%"+queryName+"%"));
		predicates.add(builder.like(customer.get(Customer_.socialReason), "%"+queryName+"%"));
		predicates.add(builder.like(customer.get(Customer_.id), "%"+queryName+"%"));
		Predicate orPredicate=builder.or(predicates.toArray(new Predicate[]{}));
		query.where(orPredicate);
		TypedQuery<Customer> typedQuery=entityManager.createQuery(query);
		return typedQuery.getResultList();
	}

	public void update(Customer customer) {
		entityManager.merge(customer);
	}

	public List<Customer> findAll() {
		return getResultListFromNamedQuery(Customer.class, "Customer.findAll");
	}

}
