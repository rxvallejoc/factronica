/**
 * 
 */
package com.obiectumclaro.factronica.core.access.key;


/**
 * Class representing an access key to be used in each invoice.
 * 
 * @author iapazmino
 *
 */
public class AccessKey {
	
	private String issued;
	private String documentType;
	private String ruc;
	private String environment;
	private String serial;
	private String sequence;
	private String generatedCode;
	private String issuingMode;
	private String checkDigit;
	
	public String getKeyRoot() {
		return issued.concat(documentType)
				.concat(ruc)
				.concat(environment)
				.concat(serial)
				.concat(sequence)
				.concat(generatedCode)
				.concat(issuingMode);
	}

	public String getFullKey() {
		return getKeyRoot().concat(checkDigit);
	}

	@Override
	public String toString() {
		return getFullKey();
	}

	public String getIssued() {
		return issued;
	}

	public void setIssued(String issued) {
		this.issued = issued;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getGeneratedCode() {
		return generatedCode;
	}

	public void setGeneratedCode(String generatedCode) {
		this.generatedCode = generatedCode;
	}

	public String getIssuingMode() {
		return issuingMode;
	}

	public void setIssuingMode(String issuingMode) {
		this.issuingMode = issuingMode;
	}

	public String getCheckDigit() {
		return checkDigit;
	}

	public void setCheckDigit(String checkDigit) {
		this.checkDigit = checkDigit;
	}
	
}
