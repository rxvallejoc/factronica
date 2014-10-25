/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.users;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import com.obiectumclaro.factronica.core.model.User;
import com.obiectumclaro.factronica.core.service.UsersBean;
import com.obiectumclaro.factronica.pos.backing.BaseBackingBean;

/**
 * @author fausto
 * 
 */
@Named
public class ListUserBackingBean extends BaseBackingBean {

	@Inject
	private UsersBean users;

	private List<User> allUsers;

	@PostConstruct
	public void init() {
		allUsers = users.findAllUsers();
	}

	public String goToEditUser(String username) {
		String navRule = String.format("/users/edit?username=%sfaces-redirect=true", username);
		return navRule;
	}

	public List<User> getAllUsers() {
		return allUsers;
	}
}
