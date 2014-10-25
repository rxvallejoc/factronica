/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.access;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.obiectumclaro.factronica.core.model.User;
import com.obiectumclaro.factronica.core.service.UsersBean;
import com.obiectumclaro.factronica.pos.backing.BaseBackingBean;

/**
 * @author fausto
 * 
 */
@Named
public class AccessBackingBean extends BaseBackingBean {

	@Inject
	private UsersBean userService;

	@Inject
	private UserSession userSession;

	private String username;
	private String password;

	public String login() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		if (getUserPrincipal() != null) {
			addFacesMessage(FacesMessage.SEVERITY_ERROR, "Ya existe un usuario logeado");
			return null;
		}
		try {
			request.login(username, password);
		} catch (Exception e) {
			addFacesMessage(FacesMessage.SEVERITY_ERROR, "Usario o Clave incorrecta ");
			return null;
		}

		userSession.setUser(null);
		String navRule = null;
		User user = userService.findUser(username);
		if (user == null) {
			addFacesMessage(FacesMessage.SEVERITY_ERROR, String.format("No existe un usuario con nombre de usuario [%s]", username));
		} else {
			userSession.setUser(user);
			navRule = "/index?faces-redirect=true";
		}

		return navRule;
	}

	public String logout() {
		String navRule = null;
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		try {
			request.logout();
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			navRule = "/login?faces-redirect=true";
		} catch (ServletException e) {
			addFacesMessage(FacesMessage.SEVERITY_ERROR, "Error al salir de la aplicacion");
		}

		return navRule;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserSession getUserSession() {
		return userSession;
	}

	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}

}
