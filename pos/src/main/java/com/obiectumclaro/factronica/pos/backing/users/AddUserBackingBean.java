/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.users;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import com.obiectumclaro.factronica.core.model.Role;
import com.obiectumclaro.factronica.core.model.User;
import com.obiectumclaro.factronica.core.service.UsersBean;
import com.obiectumclaro.factronica.core.service.exception.UserSavingException;
import com.obiectumclaro.factronica.pos.backing.BaseBackingBean;

/**
 * @author fausto
 * 
 */
@Named
public class AddUserBackingBean extends BaseBackingBean {

	@Inject
	private UsersBean users;

	private List<String> roleIds;
	private List<Role> roles;

	@Named
	@Produces
	private User user = new User();

	@PostConstruct
	public void init() {
		roleIds = new ArrayList<String>();
		roles = users.findAllRoles();
	}

	public String addUser() {
		String navRule = null;
		try {
			users.createUser(user, getRoleIdsLong());
			addFacesMessage(FacesMessage.SEVERITY_INFO, String.format("Usuario: %s creado", user.getUsername()));
			navRule = String.format("/users/list?faces-redirect=true");
		} catch (UserSavingException e) {
			addFacesMessage(FacesMessage.SEVERITY_ERROR,
					String.format("No se creo el usuario, es posible que exista otro con el mismo nombre de usuario: %s", e.getMessage()));
		}

		return navRule;
	}

	private List<Long> getRoleIdsLong() {
		List<Long> result = new ArrayList<>();
		for (String roleId : roleIds) {
			result.add(Long.valueOf(roleId));
		}

		return result;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}

	public List<Role> getRoles() {
		return roles;
	}

}
