/**
 * 
 */
package com.obiectumclaro.factronica.core.importing.invoices.tab;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

/**
 * This producer will queue the submission of a document to be received.
 * 
 * @author ipazmino
 * 
 */
@Stateless
public class InvoiceSubmissionProducer {

    @Resource(lookup = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/jms/queue/FactronicaInvoiceSubmission")
    private Queue invoiceSubmissionQueue;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void queueInvoiceSubmission(final InvoiceSubmissionMessage submissionMessage) {

	if (submissionMessage == null) {
	    throw new IllegalArgumentException("Mail message must be specified");
	}

	Connection connection = null;
	try {
	    connection = connectionFactory.createConnection();
	    final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	    final MessageProducer producer = session.createProducer(invoiceSubmissionQueue);
	    final ObjectMessage jmsMessage = session.createObjectMessage(submissionMessage);
	    producer.send(jmsMessage);
	} catch (final JMSException jmse) {
	    throw new RuntimeException("Could not deliver mail message to the outgoing queue", jmse);
	} finally {
	    close(connection);
	}
    }

    private void close(final Connection connection) {
	try {
	    if (connection != null) {
		connection.close();
	    }
	} catch (final Exception e) {
	    throw new RuntimeException(e);
	}
    }
}
