/**
 * 
 */
package com.obiectumclaro.factronica.core.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Sum of the taxable bases and taxed values for a tax type. <br />
 * <br />
 * A {@link TotalizedTax} is equal to another one if they have the same tax code
 * and the same rate code.
 * 
 * @author iapazmino
 * 
 */
public class TotalizedTax implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long taxCode;
	private String rateCode;
        private String taxDescription;
        private Long rate;
	private BigDecimal taxableBase;
	private BigDecimal taxedValue;
        private BigDecimal discount;
	public TotalizedTax() {
	}
	
//	public TotalizedTax(final Long taxCode, final String rateCode) {
//		this.taxCode = taxCode;
//		this.rateCode = rateCode;
//	}

        public TotalizedTax(final Long taxCode, final String rateCode) {
		this.taxCode = taxCode;
		this.rateCode = rateCode;
	}
	public BigDecimal getTaxableBase() {
		return taxableBase;
	}

	public void setTaxableBase(BigDecimal taxableBase) {
		this.taxableBase = taxableBase;
	}

	public Long getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(Long taxCode) {
		this.taxCode = taxCode;
	}

	public String getRateCode() {
		return rateCode;
	}

	public void setRateCode(String rateCode) {
		this.rateCode = rateCode;
	}

	public BigDecimal getTaxedValue() {
		return taxedValue;
	}

	public void setTaxedValue(BigDecimal taxedValue) {
		this.taxedValue = taxedValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((rateCode == null) ? 0 : rateCode.hashCode());
		result = prime * result + ((taxCode == null) ? 0 : taxCode.hashCode());
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		TotalizedTax other = (TotalizedTax) obj;
		if (rateCode == null) {
			if (other.rateCode != null) {
				return false;
			}
		} else if (!rateCode.equals(other.rateCode)) {
			return false;
		}
		if (taxCode == null) {
			if (other.taxCode != null) {
				return false;
			}
		} else if (!taxCode.equals(other.taxCode)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TotalizedTax [taxCode=").append(taxCode)
				.append(", rateCode=").append(rateCode)
				.append(", taxableBase=").append(taxableBase)
				.append(", taxedValue=").append(taxedValue).append("]");
		return builder.toString();
	}

    /**
     * @return the discount
     */
    public BigDecimal getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    /**
     * @return the taxDescription
     */
    public String getTaxDescription() {
        return taxDescription;
    }

    /**
     * @param taxDescription the taxDescription to set
     */
    public void setTaxDescription(String taxDescription) {
        this.taxDescription = taxDescription;
    }

    /**
     * @return the descriptionTax
     */
    public Long getRate() {
        return rate;
    }

    /**
     * @param descriptionTax the descriptionTax to set
     */
    public void setRate(Long rate) {
        this.rate = rate;
    }

}
