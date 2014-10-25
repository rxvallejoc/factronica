/**
 * 
 */
package com.obiectumclaro.factronica.core.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author rvallejo
 *
 */
@Entity
@Table(name = "DocumentReport")
public class DocumentReport {
	@Id
	@GeneratedValue
	private Long id;
	private String archive;
	@Temporal(TemporalType.DATE)
	private Date date;
	private String line;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getArchive() {
		return archive;
	}
	public void setArchive(String archive) {
		this.archive = archive;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	
	

}
