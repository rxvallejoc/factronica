/**
 * 
 */
package com.obiectumclaro.factronica.core.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author iapazmino
 * 
 */
@NamedQuery(name = "emailTemplate.findByKeyword", query = "select et from EmailTemplate et where et.keyword = :keyword")
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "keyword" }) })
public class EmailTemplate {

	@Id
	@GeneratedValue
	private long pk;

	private String subject;
	@Size(max = 2048)
	private String text;
	@NotNull
	private String keyword;

	public EmailTemplate() {
		this(null, null);
	}
	/**
	 * Constructs an instance with its subject and email text initialized.
	 * 
	 * @param subject
	 * @param text
	 */
	public EmailTemplate(final String subject, final String text) {
		this.subject = subject;
		this.text = text;
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

	public long getPk() {
		return pk;
	}

	public void setPk(long pk) {
		this.pk = pk;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
