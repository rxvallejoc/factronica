/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.obiectumclaro.factronica.pos.jsf.FacesMessageHelper;

/**
 * Holder of common resources all backing beans need to be provided with. It's
 * request scoped in order to encourage the implementation of the PRG pattern.
 * 
 * @author iapazmino
 * 
 */
@RequestScoped
public abstract class BaseBackingBean {

	@Inject
	private FacesContext facesContext;

	/**
	 * @return the current {@link FacesContext}.
	 */
	public FacesContext getFacesContext() {
		return facesContext;
	}

	public void setFacesContext(FacesContext facesContext) {
		this.facesContext = facesContext;
	}

	/**
	 * @return the flash context for the current request
	 */
	public Flash getFlashMap() {
		return getFacesContext().getExternalContext().getFlash();
	}

	/**
	 * Adds a new Faces Message to Faces Context
	 * 
	 * @param severity
	 * @param summary
	 * @param detail
	 */
	public void addFacesMessage(final Severity severity, final String summary,
			final String detail) {
		FacesMessageHelper.addFacesMessage(severity, summary, detail);
	}

	/**
	 * Adds a new Faces Message without detail to Faces Context.
	 * 
	 * @param severity
	 * @param summary
	 */
	public void addFacesMessage(final Severity severity, final String messageText) {
		addFacesMessage(severity, messageText, null);
	}
	
	/**
	 * Adds a new Info message to the faces context.
	 * 
	 * @param summary
	 * @param detail
	 */
	public void addInfo(final String summary, final String detail) {
		addFacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
	}
	
	/**
	 * Adds a new Error message to the faces context.
	 * 
	 * @param summary
	 * @param detail
	 */
	public void addError(final String summary, final String detail) {
		addFacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
	}

	public String getRequestParameter(final String parameterName) {
		return getFacesContext().getExternalContext().getRequestParameterMap().get(parameterName);
	}

	protected String getUserPrincipal() {
		Principal p = getFacesContext().getExternalContext().getUserPrincipal();
		return p == null ? null : p.getName();
	}

	protected void downloadFile(byte[] archivo, String tipoContenido, String nombreArchivo) throws IOException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

		response.reset();
		response.setContentType(tipoContenido);
		response.setHeader("Content-disposition", "attachment; filename=\"" + nombreArchivo + "\"");

		OutputStream output = response.getOutputStream();
		output.write(archivo);
		output.close();

		facesContext.responseComplete();
	}
        
        protected HttpServletRequest getHttpRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    /**
     * @return
     */
    protected HttpServletResponse getHttpResponse() {
        return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }

}
