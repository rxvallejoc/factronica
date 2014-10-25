/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.service.exception;

/**
 *
 * @author maza261109
 */
public class InvoiceServiceException extends Exception{
        /* 
	 */
	private static final long serialVersionUID = 1L;

	public InvoiceServiceException(String message){
		super(message);
	}
	
	public InvoiceServiceException(Throwable t){
		super(t);
	}
	
	public InvoiceServiceException(String message, Throwable t){
		super(message, t);
	}
}
