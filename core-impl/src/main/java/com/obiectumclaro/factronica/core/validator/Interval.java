/**
 * 
 */
package com.obiectumclaro.factronica.core.validator;

import java.io.Serializable;

/**
 * Object used to define an interval of numbers. By default it includes the
 * endpoints, but this can be changed setting inclusive to false.
 * 
 * @author iapazmino
 * 
 */
public class Interval implements Serializable {

	private static final long serialVersionUID = 1L;

	private int min;
	private int max;
	private boolean inclusive = true;

	/**
	 * Construct an instance of a range defining its min and max.
	 * 
	 * @param min
	 *            is the minimum value to be accepted.
	 * @param max
	 *            is the maximum value to be accepted.
	 */
	public Interval(final int min, final int max) {
		this.min = min;
		this.max = max;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * @return whether or not the endpoints are to be included in any
	 *         calculation the interval is in.
	 */
	public boolean isInclusive() {
		return inclusive;
	}

	public void setInclusive(boolean inclusive) {
		this.inclusive = inclusive;
	}

}
