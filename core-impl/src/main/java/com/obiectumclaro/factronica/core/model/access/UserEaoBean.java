/**
 * 
 */
package com.obiectumclaro.factronica.core.model.access;

import java.util.List;

import javax.ejb.Stateless;

import com.obiectumclaro.factronica.core.model.User;

/**
 * @author fausto
 * 
 */
@Stateless
public class UserEaoBean extends BaseEaoBean {

	public void save(User user) {
		entityManager.persist(user);
	}

	public void update(User user) {
		entityManager.merge(user);
	}

	public List<User> findAll() {
		return getResultListFromNamedQuery(User.class, "User.findAll");
	}

	public User findById(String username) {
		return getSingleResultFromNamedQuery(User.class, "User.findById", username);
	}
}
