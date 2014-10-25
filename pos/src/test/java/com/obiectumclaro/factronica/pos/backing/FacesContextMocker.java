/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * {@link FacesContext}'s mock.
 * 
 * @see <a
 *      href="http://illegalargumentexception.blogspot.com/2011/12/jsf-mocking-facescontext-for-unit-tests.html#mockFacesCurrentInstance">
 *      JSF: mocking FacesContext for unit tests</a>
 * @author iapazmino
 * 
 */
public abstract class FacesContextMocker extends FacesContext {

	private FacesContextMocker() {
	}

	private static final Release RELEASE = new Release();

	private static class Release implements Answer<Void> {
		@Override
		public Void answer(InvocationOnMock invocation) throws Throwable {
			setCurrentInstance(null);
			return null;
		}
	}

	public static FacesContext mockFacesContext() {
		final FacesContext context = mock(FacesContext.class);		
		final ExternalContext externalContext = mock(ExternalContext.class);
		final Flash flash = mock(Flash.class);
		
		setCurrentInstance(context);
		
		when(context.getExternalContext()).thenReturn(externalContext);
		when(externalContext.getFlash()).thenReturn(flash);
		doNothing().when(flash).setKeepMessages(anyBoolean());
		doAnswer(RELEASE).when(context).release();
		
		return context;
	}

}
