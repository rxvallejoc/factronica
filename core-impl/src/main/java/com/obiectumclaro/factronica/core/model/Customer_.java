/**
 * 
 */
package com.obiectumclaro.factronica.core.model;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.obiectumclaro.factronica.core.enumeration.IdType;

/**
 * Entity meta model for {@link Customer}
 * @author Obiectumclaro
 *
 */
@StaticMetamodel(Customer.class)
public class Customer_ {
	public static volatile SingularAttribute<Customer, Long> pk;
	public static volatile SingularAttribute<Customer, String> address;
	public static volatile SingularAttribute<Customer, String> email;
	public static volatile SingularAttribute<Customer, String> id;
	public static volatile SingularAttribute<Customer, IdType> idType;
	public static volatile SingularAttribute<Customer, String> name;
	public static volatile SingularAttribute<Customer, String> lastName;
	public static volatile SingularAttribute<Customer, String> socialReason;
	public static volatile SingularAttribute<Customer, String> phone;
	public static volatile SingularAttribute<Customer, Agreement> agreement;
	public static volatile ListAttribute<Customer, Invoice> invoices;
}
