/**
 * 
 */
package com.obiectumclaro.factronica.core.model.access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import com.obiectumclaro.factronica.core.model.Role;

/**
 * @author fausto
 * 
 */
@Stateless
public class RoleEaoBean extends BaseEaoBean {

	public List<Role> findAll() {
		return getResultListFromNamedQuery(Role.class, "Role.findAll");
	}

	public List<Role> findByIds(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return new ArrayList<Role>();
		}
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ids", ids);
		return getResultListFromQuery(Role.class, "select r from Role r where r.pk in (:ids)", parameters);
	}
}
