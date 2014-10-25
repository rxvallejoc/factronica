/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.users;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.obiectumclaro.factronica.core.model.Role;
import com.obiectumclaro.factronica.core.model.User;
import com.obiectumclaro.factronica.core.service.UsersBean;
import com.obiectumclaro.factronica.core.service.exception.UserSavingException;
import com.obiectumclaro.factronica.pos.jsf.FacesMessageHelper;

/**
 * @author fausto
 * 
 */
@ManagedBean
@ViewScoped
public class EditUserBackingBean {

	@Inject
	private UsersBean users;

	private User user;
	private List<String> roleIds;
	private List<Role> roles;

	@PostConstruct
	public void init() {
		String username = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("username");
		if (username == null) {
			FacesMessageHelper.addFacesMessage(FacesMessage.SEVERITY_ERROR, "No se encontro el parametro 'username'");
		} else {
			validateUser(username);
		}
	}

	private void validateUser(String username) {
		user = users.findUser(username);
		if (user == null) {
			FacesMessageHelper.addFacesMessage(FacesMessage.SEVERITY_ERROR, String.format("No se encontro ningun usuario con username %s", username));
		} else {
			setRoles();
		}
	}

	private void setRoles() {
		roles = users.findAllRoles();
		roleIds = new ArrayList<String>();
		for (Role r : user.getRoles()) {
			roleIds.add(r.getPk().toString());
		}
	}

	public String saveUser() {
		String navRule = null;
		try {
			users.updateUser(user, getRoleIdsLong());
			FacesMessageHelper.addFacesMessage(FacesMessage.SEVERITY_INFO, String.format("Usuario %s actualizado", user.getUsername()));
			navRule = String.format("/users/list?faces-redirect=true");
		} catch (UserSavingException e) {
			FacesMessageHelper.addFacesMessage(FacesMessage.SEVERITY_ERROR, String.format("No se guardo el usuario: %s", e.getMessage()));
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

	public List<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
