/**
 * 
 */
package com.obiectumclaro.factronica.core.access.key;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Generation of the checking digit based on mod 11 algorithm as specified in
 * {@link http://es.wikipedia.org/wiki/C%C3%B3digo_de_control}.
 * 
 * @author iapazmino
 * 
 */
public class Mod11CheckDigitGenerator implements CheckDigitGenerator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.obiectumclaro.factronica.core.access.key.CheckDigitGenerator#
	 * getComputedDigit(java.math.BigDecimal)
	 */
	@Override
	public Integer getComputedDigit(final BigDecimal root) {
		final String reverse = reverse(root);
		final List<Integer> addends = fetchFrom(reverse);
		final Long sum = sum(addends);
		final int mod11 = mod11(sum);
		return substractFrom11(mod11);
	}
	
	private String reverse(final BigDecimal root) {
		final StringBuilder reverse = new StringBuilder(root.toPlainString());
		return reverse.reverse().toString();
	}
	
	private List<Integer> fetchFrom(final String reverse) {
		final List<Integer> addends = new ArrayList<>();
		int toInt = 0;
		int multiplier = 2;
		for (int i = 0; i < reverse.length(); i++) {
			toInt = toInt(reverse.charAt(i));
			addends.add(toInt * multiplier);
			multiplier = adjust(multiplier);
		}
		return addends;
	}
	
	private int toInt(final char current) {
		final char[] charAtI = new char[1];
		charAtI[0] = current;
		return Integer.valueOf(new String(charAtI));
	}
	
	private int adjust(int multiplier) {
		return ++multiplier > 7 ? 2 : multiplier;
	}
	
	private Long sum(final List<Integer> addends) {
		long sum = 0;
		for (Integer addend : addends) {
			sum += addend;
		}
		return sum;
	}
	
	private int mod11(final Long sum) {
		final Long mod11 = sum % 11;
		return mod11.intValue();
	}
	
	private int substractFrom11(final int mod11) {
		final int substraction = 11 - mod11;
		if (substraction == 11) {
			return 0;
		} else if (substraction == 10) {
			return 1;
		} else {
			return substraction;
		}
	}

}
