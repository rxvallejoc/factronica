/**
 * 
 */
package com.obiectumclaro.factronica.core.model.access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import com.obiectumclaro.factronica.core.model.TaxValue;

/**
 * @author faustodelatog
 * 
 */
@Stateless
public class TaxValueEaoBean extends BaseEaoBean {

	public List<TaxValue> findAll() {
		return getResultListFromNamedQuery(TaxValue.class, "TaxValue.findAll");
	}

	public List<TaxValue> findByIds(List<Long> taxValueIds) {
		if (taxValueIds == null || taxValueIds.isEmpty()) {
			return new ArrayList<>();
		}
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("ids", taxValueIds);
		return getResultListFromQuery(TaxValue.class, "select tv from TaxValue tv where tv.pk in (:ids)", parameters);
	}

	public TaxValue findById(final Long id) {
		final Map<String, Object> params = new HashMap<>();
		params.put("pk", id);
		return getSingleResultFromNamedQuery(TaxValue.class, "TaxValue.findByPk", params);
	}

	public TaxValue findByCode(final String code) {
		final Map<String, Object> params = new HashMap<>();
		params.put("code", code);
		return getSingleResultFromNamedQuery(TaxValue.class, "TaxValue.findByCode", params);
	}

}
