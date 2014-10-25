/**
 * 
 */
package com.obiectumclaro.factronica.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * @author iapazmino
 * 
 */
@Entity
public class Email implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private long pk;

	@OneToMany
	private List<Customer> destinataries;
	private String subject;
	private String text;

	public Email() {
		this(null, null);
	}
	
	public Email(final String subject, final String text) {
		this.destinataries = new ArrayList<Customer>();
		this.subject = subject;
		this.text = text;
	}

	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public List<Customer> getDestinataries() {
		return destinataries;
	}

	public void setDestinataries(List<Customer> Destinataries) {
		this.destinataries = Destinataries;
	}
	
	public void addDestinatary(final Customer destinatary) {
		destinataries.add(destinatary);
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Email [to=").append(destinataries).append(", subject=")
				.append(subject).append("]");
		return builder.toString();
	}

}
