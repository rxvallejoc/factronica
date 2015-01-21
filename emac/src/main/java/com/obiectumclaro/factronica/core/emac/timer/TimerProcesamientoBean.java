/*
 * TimerMonitorEntidadCertificadora.java
 *
 * Copyright (c) 2012 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */

package com.obiectumclaro.factronica.core.emac.timer;

import com.obiectumclaro.factronica.core.emac.InvoiceBuilder;
import com.obiectumclaro.factronica.core.emac.access.EmacInvoiceBean;
import com.obiectumclaro.factronica.core.model.Issuer;
import com.obiectumclaro.factronica.core.service.IssuerBean;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timer;
import java.util.concurrent.Future;


@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)

public class TimerProcesamientoBean {

	private static final String EVERY = "*";
	private static final String ONE = "*/60";
	private static final Logger LOG = Logger.getLogger(TimerProcesamientoBean.class);
	@EJB
	private EmacInvoiceBean emacInvoiceBean;
	@EJB
	private InvoiceBuilder invoiceBuilder;
	@EJB
	private IssuerBean issuerBean;
	@EJB
	private EmacDocumentSubmissionProducer producer;
	private Issuer issuer;

	@PostConstruct
	void inicializar() {
		try {
			this.issuer = issuerBean.getIssuerById(1L);
		} catch (Exception e) {
			LOG.error("No existe configurado el Emisor", e);
		}
		LOG.info("-------------------------------------------------------------------");
		LOG.info("Inicializacion de Timer para EMAC");
		LOG.info("-------------------------------------------------------------------");
	}

	@Schedule(dayOfMonth = EVERY, month = EVERY, year = EVERY, second = ONE, minute = EVERY, hour = EVERY, persistent = false)
	public void reprocesarComprobantes(final Timer timer) {
			process();
	}


	@Asynchronous
	public Future<Integer> process(){
		LOG.info("Inicia Envio de Solicitudes SRI");
		emacInvoiceBean.getDocumentsForRequest().forEach(emacDocument -> {
			EmacDocumentSubmissionMessage message = null;
			if(emacDocument.getCoddoc().equals("01")){
				message = invoiceBuilder.generateXMLFile(emacDocument, issuer);
			}
			producer.queueDocumentSubmission(message);
		});
		LOG.info("Finaliza Envio de Solicitudes SRI");
		return new AsyncResult<Integer>(0);

	}





}
