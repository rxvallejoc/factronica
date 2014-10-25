/**
 * 
 */
package com.obiectumclaro.factronica.core.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.crypto.CryptoUtil;
import com.obiectumclaro.factronica.core.model.Role;
import com.obiectumclaro.factronica.core.model.User;
import com.obiectumclaro.factronica.core.model.User.Status;
import com.obiectumclaro.factronica.core.model.access.RoleEaoBean;
import com.obiectumclaro.factronica.core.model.access.UserEaoBean;
import com.obiectumclaro.factronica.core.service.exception.UserSavingException;

/**
 * @author fausto
 * 
 */
@Stateless
public class UsersBean {

	@EJB
	private UserEaoBean userEao;

	@EJB
	private RoleEaoBean roleEao;

	public User findUser(String username) {
		User user = userEao.findById(username);
		if (user != null) {
			user.getRoles().size();
		}
		return user;

	}

	public List<Role> findAllRoles() {
		return roleEao.findAll();
	}

	public List<User> findAllUsers() {
		return userEao.findAll();
	}

	public void createUser(User user, List<Long> roleIds) throws UserSavingException {
		User u = userEao.findById(user.getUsername());
		if (u != null) {
			throw new UserSavingException(String.format("El nombre de usuario %s esta asociado a otro usuario", user.getUsername()));
		}

		user.setPassword(CryptoUtil.createPasswordHash("MD5", "Base64", "UTF-8", "", user.getPassword()));
		user.setRoles(roleEao.findByIds(roleIds));
		user.setStatus(Status.ACT);
		userEao.save(user);
	}

	public void updateUser(User user, List<Long> roleIds) throws UserSavingException {
		User u = userEao.findById(user.getUsername());
		if (u != null && !u.getPk().equals(user.getPk())) {
			throw new UserSavingException(String.format("El nombre de usuario %s esta asociado a otro usuario", user.getUsername()));
		}

		String password = CryptoUtil.createPasswordHash("MD5", "Base64", "UTF-8", "", user.getPassword());
		if (user.getPassword() == null || user.getPassword().length() == 0) {
			User existent = userEao.findById(user.getUsername());
			password = existent == null ? password : existent.getPassword();
		}
		user.setPassword(password);
		if (roleIds != null) {
			user.setRoles(roleEao.findByIds(roleIds));
		}
		userEao.update(user);
	}
}
