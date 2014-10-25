/**
 * 
 */
package com.obiectumclaro.factronica.core.access.key;

import java.math.BigDecimal;

/**
 * Definition for check digit generation strategy.
 * 
 * @author iapazmino
 * 
 */
public interface CheckDigitGenerator {

	/**
	 * Computes a checking digit based on the provided root.
	 * 
	 * @param root
	 *            is the number used for the generation.
	 * 
	 * @return the computed check digit for the given root.
	 */
	Integer getComputedDigit(BigDecimal root);

}
