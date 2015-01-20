/**
 * 
 */
package com.obiectumclaro.factronica.core.access.key;

import com.obiectumclaro.factronica.core.enumeration.DocumentType;
import com.obiectumclaro.factronica.core.enumeration.Environment;
import com.obiectumclaro.factronica.core.enumeration.IssuingMode;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class used to build access keys.
 * 
 * @author iapazmino
 *
 */
public class AccessKeyBuilder {
	
	static final DateFormat FORMATTER_DATE = new SimpleDateFormat("ddMMyyyy");
	static final NumberFormat FORMATTER_DECIMAL = new DecimalFormat("00000000");
	
	private static final String ERROR_LENGTH = "Value for %s should be a number %s digits long";
	private static final String ERROR_NOT_SET = "Value for %s can't be null or empty";
	
	private AccessKey accessKey;
	private CheckDigitGenerator checkDigitGenerator;
	
	public AccessKeyBuilder() {
		accessKey = new AccessKey();
		checkDigitGenerator = new Mod11CheckDigitGenerator();
	}

	public AccessKey createAccessKey() {
		checkForRuc();
		checkForSerial();
		checkForSequence();
		assignDefaultDate();
		assignDefaultType();
		assignDefaultEnvironment();
		assignDefaultGeneratedCode();
		assignDefaultIssuingMode();
		computeCheckDigit();
		return accessKey;
	}

	public AccessKeyBuilder issued(final Date issued) {
		accessKey.setIssued(FORMATTER_DATE.format(issued));
		return this;
	}

	public AccessKeyBuilder type(final DocumentType type) {
		accessKey.setDocumentType(type.code);
		return this;
	}
	
	public AccessKeyBuilder ruc(final String ruc) {
		checkForExpectedLength("ruc", ruc, 13);
		accessKey.setRuc(ruc);
		return this;
	}
	
	public AccessKeyBuilder environment(final Environment environment) {
		accessKey.setEnvironment(Integer.toString(environment.ordinal()));
		return this;
	}
	
	public AccessKeyBuilder serial(final String serial) {
		checkForExpectedLength("serial", serial, 6);
		accessKey.setSerial(serial);
		return this;
	}
	
	public AccessKeyBuilder sequence(final String sequence) {
		checkForExpectedLength("sequence", sequence, 9);
		accessKey.setSequence(sequence);
		return this;
	}
	
	public AccessKeyBuilder generatedCode(final Long generatedCode) {
		final String code = FORMATTER_DECIMAL.format(generatedCode);
		accessKey.setGeneratedCode(code.substring(0, 8));
		return this;
	}
	
	public AccessKeyBuilder issuingMode(final IssuingMode issuingMode) {
		accessKey.setIssuingMode(Integer.toString(issuingMode.ordinal()+1));
		return this;
	}
	
	private void checkForRuc() {
		if (isNullOrEmpty(accessKey.getRuc())) {
			throw new IllegalArgumentException(String.format(ERROR_NOT_SET, "null"));
		}
	}
	
	private void checkForSerial() {
		if (isNullOrEmpty(accessKey.getSerial())) {
			throw new IllegalArgumentException(String.format(ERROR_NOT_SET, "serial"));
		}
	}
	
	private void checkForSequence() {
		if (isNullOrEmpty(accessKey.getSequence())) {
			throw new IllegalArgumentException(String.format(ERROR_NOT_SET, "sequence"));
		}
	}
	
	private void assignDefaultDate() {
		if (isNullOrEmpty(accessKey.getIssued())) {
			issued(Calendar.getInstance().getTime());
		}
	}
	
	private void assignDefaultType() {
		if (isNullOrEmpty(accessKey.getDocumentType())) {
			type(DocumentType.FACTURA);
		}
	}
	
	private void assignDefaultEnvironment() {
		if (isNullOrEmpty(accessKey.getEnvironment())) {
			environment(Environment.PRODUCCION);
		}
	}
	
	private void assignDefaultGeneratedCode() {
		if (isNullOrEmpty(accessKey.getGeneratedCode())) {
			generatedCode(Calendar.getInstance().getTimeInMillis());
		}
	}
	
	private void assignDefaultIssuingMode() {
		if (isNullOrEmpty(accessKey.getIssuingMode())) {
			issuingMode(IssuingMode.NORMAL);
		}
	}
	
	private void computeCheckDigit() {
		final BigDecimal root = new BigDecimal(accessKey.getKeyRoot());
		final Integer chackDigit = checkDigitGenerator.getComputedDigit(root);
		accessKey.setCheckDigit(chackDigit.toString());
	}
	
	private boolean isNullOrEmpty(final String string) {
		return null == string || string.isEmpty();
	}
	
	private void checkForExpectedLength(final String field, final String value, 
			final int length) {
		if (null == value || value.length() != length) {
			throw new IllegalArgumentException(String.format(ERROR_LENGTH, field, length));
		}
	}

	public CheckDigitGenerator getCheckDigitGenerator() {
		return checkDigitGenerator;
	}

	public void setCheckDigitGenerator(CheckDigitGenerator checkDigitGenerator) {
		this.checkDigitGenerator = checkDigitGenerator;
	}

}
