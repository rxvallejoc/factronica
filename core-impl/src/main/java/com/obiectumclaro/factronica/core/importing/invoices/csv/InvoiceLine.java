/**
 * 
 */
package com.obiectumclaro.factronica.core.importing.invoices.csv;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.obiectumclaro.factronica.core.enumeration.IdType;

/**
 * @author iapazmino
 * 
 */
public class InvoiceLine implements Serializable {

	private static final Integer EXPECTED_COLUMNS = 5;
	private static final String DATE_PATTERN = "dd/MM/yyyy";

	private IdType idType;
	private String id;
	private String issuedDate;
	private BigDecimal quantity;
	private String productCode;
	private Map<String, String> additionalInfo;

	private final String[] original;
	private LineStatus status;
	private List<String> messages;
	private String accessKey;

	private InvoiceLine(final String[] original) {
		this.original = original;
		this.additionalInfo = new HashMap<>();
		this.messages = new ArrayList<>();
	}

	public static InvoiceLine buildLine(final String[] tokens) {
		final InvoiceLine line = new InvoiceLine(tokens);

		final int cols = tokens.length;
		if (cols >= EXPECTED_COLUMNS) {
			line.setStatus(LineStatus.ENVIADA);
			line.convertIdType();
			line.convertId();
			line.convertIssuedDate();
			line.convertProductQuantity();
			line.convertProductCode();
			if (cols > EXPECTED_COLUMNS) {
				line.convertAdditionalInfo(cols);
			}
		} else {
			final String error = String.format("Cada fila debe tener al menos %s columnas", EXPECTED_COLUMNS);
			line.addErrorMessage(error);
		}

		return line;
	}

	public void convertIdType() {
		try {
			final String value = original[0];
			if (isNullOrEmpty(value)) {
				addErrorMessage("El tipo de identificacion no puede estar nulo o vacio");
				return;
			}
			setIdType(IdType.valueOf(value.toUpperCase()));
		} catch (IllegalArgumentException iae) {
			addErrorMessage("Los tipos de identificacion validos son CEDULA, RUC, PASAPORTE");
			return;
		}
	}

	public void convertId() {
		String value = original[1];
		if (isNullOrEmpty(value)) {
			addErrorMessage("El numero de identificacion no puede estar nulo o vacio");
			return;
		}
		if (value.length() == 9 && IdType.CEDULA.equals(getIdType())) {
			value = "0" + value;
		}
		setId(value);
	}

	public void convertIssuedDate() {
		try {
			final String value = original[2];
			if (isNullOrEmpty(value)) {
				addErrorMessage("La fecha de emision no puede estar nulo o vacio");
				return;
			}
			new SimpleDateFormat(DATE_PATTERN).parse(value);
			setIssuedDate(value);
		} catch (ParseException pe) {
			addErrorMessage("La fecha debe seguir el patron " + DATE_PATTERN);
			return;
		}
	}

	public void convertProductQuantity() {
		final String value = original[3];
		try {
			if (isNullOrEmpty(value)) {
				addErrorMessage("La cantidad de productos no puede estar nulo o vacio");
				return;
			}
			setQuantity(new BigDecimal(value));
		} catch (NumberFormatException nfe) {
			final String error = "Se esperaba un numero para la cantidad de productos pero se obtuvo [%s]";
			addErrorMessage(String.format(error, value));
			return;
		}
	}

	public void convertProductCode() {
		final String value = original[4];
		if (isNullOrEmpty(value)) {
			addErrorMessage("El codigo del producto no puede estar nulo o vacio");
			return;
		}
		setProductCode(value);
	}
	
	public void convertAdditionalInfo(final int cols) {
		final String includesOneEqualsSign = ".*=.*";
		String value = null;
		int equalsIndex = 0;
		for (int i = 5; i < cols; i++) {
			value = original[i];
			if (!isNullOrEmpty(value) && !value.matches(includesOneEqualsSign)) {
				addErrorMessage("La informacion adicional debe tener el formato nombre=valor");
				return;
			}
			equalsIndex = value.indexOf("=");
			additionalInfo.put(value.substring(0, equalsIndex), value.substring(equalsIndex + 1));
		}
	}

	private boolean isNullOrEmpty(final String string) {
		return null == string || string.isEmpty();
	}

	public void addErrorMessage(final String message) {
		setStatus(LineStatus.ERROR);
		addMessage(message);
	}
	
	public String toCSV() {
		String csv = "";
		for (int i = 0; i < original.length; i++) {
			csv += original[i] + ",";
		}
		csv += status + ",";
		
		for (String message : messages) {
			csv += message + ",";
		}
		
		return csv;
	}

	public IdType getIdType() {
		return idType;
	}

	public void setIdType(IdType idType) {
		this.idType = idType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(String issuedDate) {
		this.issuedDate = issuedDate;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public LineStatus getStatus() {
		return status;
	}

	public void setStatus(LineStatus status) {
		this.status = status;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void addMessage(final String message) {
		this.messages.add(message);
	}

	public static Integer getExpectedColumns() {
		return EXPECTED_COLUMNS;
	}

	public String[] getOriginal() {
		return original;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public Map<String, String> getAdditionalInfo() {
		return additionalInfo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvoiceLine [idType=").append(idType).append(", id=")
				.append(id).append(", issuedDate=").append(issuedDate)
				.append(", quantity=").append(quantity)
				.append(", productCode=").append(productCode)
				.append(", status=").append(status).append(", messages=")
				.append(messages).append("]");
		return builder.toString();
	}

}
