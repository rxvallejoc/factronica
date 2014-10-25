package com.obiectumclaro.factronica.core.web.service.sri.client;

import java.util.List;

import com.obiectumclaro.factronica.core.web.service.sri.client.reception.Comprobante;
import com.obiectumclaro.factronica.core.web.service.sri.client.authorization.Autorizacion;

/**
 * Client to the IRS web services to request authorization for invoices. <br />
 * <br />
 * This client is to be used as the service is provided, asynchronous. In this
 * mode you should first request for reception of the invoice, and then, query
 * the result of the authorization. <br />
 * <br />
 * It is also provided with a synchronous-like mode where the client makes both
 * calls and waits the {@link #DEFAULT_WAITING_TIME}. This mode is used with the
 *
 * @author iapazmino
 * 
 */
public interface InvoiceAuthorization {

	String URL_REQUEST = "/comprobantes-electronicos-ws/RecepcionComprobantes?wsdl";
	String URL_QUERY = "/comprobantes-electronicos-ws/AutorizacionComprobantes?wsdl";

	/** Default waiting time between request and query is 3000 milliseconds */
	int DEFAULT_WAITING_TIME = 3000;

	AuthorizationState request(byte[] signedInvoice);

	AuthorizationState request(byte[] signedInvoice, ServiceEnvironment env);

	List<Autorizacion> syncRequest(byte[] signedInvoice)
			throws ReturnedInvoiceException;

	List<Autorizacion> syncRequest(byte[] signedInvoice, ServiceEnvironment env)
			throws ReturnedInvoiceException;

	List<Autorizacion> syncRequest(byte[] signedInvoice, ServiceEnvironment env,
			int seconds) throws ReturnedInvoiceException;

	List<Autorizacion> query(String accessKey);

	List<Autorizacion> query(String accessKey, ServiceEnvironment environment);

	List<Comprobante> getComprobantes();

}
