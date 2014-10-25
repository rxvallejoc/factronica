/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing;

import javax.faces.context.FacesContext;

import org.junit.After;
import org.junit.Before;

/**
 * Base to be extended by backing beans unit tests. It provides with basic
 * scaffolding to mock the JSF's context.
 * 
 * @author iapazmino
 * 
 */
public abstract class TestBackingBean {

	protected FacesContext fc;

	@Before
	public void mockFacesContext() {
		fc = FacesContextMocker.mockFacesContext();
	}

	@After
	public void cleanup() {
		fc.release();
	}

}
