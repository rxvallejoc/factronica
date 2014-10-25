/**
 * 
 */
package com.obiectumclaro.factronica.core.importing.products;

import java.io.Serializable;

/**
 * @author iapazmino
 * 
 */
public class HeaderLabel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String label;
	private int index;

	public HeaderLabel() {
		this(null);
	}
	
	public HeaderLabel(final String label) {
		this(label, 0);
	}

	public HeaderLabel(int index) {
		this(null, index);
	}

	public HeaderLabel(final String label, final int index) {
		this.label = label;
		this.index = index;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof HeaderLabel)) {
			return false;
		}
		HeaderLabel other = (HeaderLabel) obj;
		if (index != other.index) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HeaderLabel [label=").append(label).append(", index=")
				.append(index).append("]");
		return builder.toString();
	}

}
