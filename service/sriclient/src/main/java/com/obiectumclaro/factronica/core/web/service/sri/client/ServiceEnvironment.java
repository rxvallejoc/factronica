/**
 * 
 */
package com.obiectumclaro.factronica.core.web.service.sri.client;

/**
 * @author iapazmino
 *
 */
public enum ServiceEnvironment {

	TEST("https://celcer.sri.gob.ec"), PRODUCTION("https://cel.sri.gob.ec");
	
	final public String wsdlLocation;

	private ServiceEnvironment(final String wsdlLocation) {
		this.wsdlLocation = wsdlLocation;
	}
	
}
