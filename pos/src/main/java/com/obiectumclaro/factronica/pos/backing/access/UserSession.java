/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.access;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.obiectumclaro.factronica.core.model.User;

/**
 * @author fausto
 *
 */
@SessionScoped
@Named
public class UserSession implements Serializable {

	private static final long serialVersionUID = 1L;

	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
