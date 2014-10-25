/**
 * 
 */
package com.obiectumclaro.factronica.pos.jsf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import com.obiectumclaro.factronica.core.web.service.sri.client.MessageSeverity;

/**
 * Helper used to add messages to the current {@link FacesContext}.
 * 
 * @author iapazmino
 * 
 */
public class FacesMessageHelper {
	
	private static final Map<MessageSeverity, FacesMessage.Severity> MESSAGES_MAP = new HashMap<>();
	
	static {
		MESSAGES_MAP.put(MessageSeverity.INFORMATIVO, FacesMessage.SEVERITY_INFO);
		MESSAGES_MAP.put(MessageSeverity.ADVERTENCIA, FacesMessage.SEVERITY_WARN);
		MESSAGES_MAP.put(MessageSeverity.ERROR, FacesMessage.SEVERITY_ERROR);
	}

	/**
	 * Adds a new Faces Message to Faces Context
	 * 
	 * @param severity
	 * @param summary
	 * @param detail
	 */
	public static void addFacesMessage(final Severity severity,
			final String summary, final String detail) {
		final FacesContext facesContext = FacesContext.getCurrentInstance();
		final FacesMessage message = new FacesMessage(severity, summary, detail);
		facesContext.addMessage(null, message);
		facesContext.getExternalContext().getFlash().setKeepMessages(true);
	}

	/**
	 * Adds a new Faces Message without detail to Faces Context.
	 * 
	 * @param severity
	 * @param summary
	 */
	public static void addFacesMessage(final Severity severity,
			final String messageText) {
		addFacesMessage(severity, messageText, null);
	}

	/**
	 * Adds a new Info message to the faces context.
	 * 
	 * @param summary
	 * @param detail
	 */
	public static void addInfo(final String summary, final String detail) {
		addFacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
	}

	/**
	 * Adds a new Error message to the faces context.
	 * 
	 * @param summary
	 * @param detail
	 */
	public static void addError(final String summary, final String detail) {
		addFacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
	}

	public static void addRequestMessages(final List<com.obiectumclaro.factronica.core.web.service.sri.client.reception.Mensaje> mensajes) {
		MessageSeverity severity = null;
		for (com.obiectumclaro.factronica.core.web.service.sri.client.reception.Mensaje mensaje : mensajes) {
			severity = MessageSeverity.valueOf(mensaje.getTipo());
			addMessage(severity, mensaje.getIdentificador(), mensaje.getMensaje(), mensaje.getInformacionAdicional());
		}
	}

	public static void addQueryMessages(final List<com.obiectumclaro.factronica.core.web.service.sri.client.authorization.Mensaje> mensajes) {
		MessageSeverity severity = null;
		for (com.obiectumclaro.factronica.core.web.service.sri.client.authorization.Mensaje mensaje : mensajes) {
			severity = MessageSeverity.valueOf(mensaje.getTipo());
			addMessage(severity, mensaje.getIdentificador(), mensaje.getMensaje(), mensaje.getInformacionAdicional());
		}
	}
	
	private static void addMessage(final MessageSeverity severity, final String id, final String mensaje, final String detail) {
		final String formatedDetail = detail != null && !detail.isEmpty() ? ": " + detail : "";
		final String formatedSummary = String.format("[%s] %s %s", id, mensaje, formatedDetail);
		addFacesMessage(MESSAGES_MAP.get(severity), formatedSummary, null);
	}
	
}
