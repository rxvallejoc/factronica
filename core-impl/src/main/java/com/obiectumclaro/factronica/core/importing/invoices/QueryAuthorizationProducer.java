/**
 * 
 */
package com.obiectumclaro.factronica.core.importing.invoices;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import com.obiectumclaro.factronica.core.importing.invoices.tab.InvoiceSubmissionMessage;

/**
 * @author ipazmino
 * 
 */
@Stateless
public class QueryAuthorizationProducer {

    @Resource(lookup = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = QueryAuthorizationConstants.JNDI_QUERY_AUTHORIZATION_QUEUE)
    private Queue queryAuthQueue;

    public void queueAuthorizationQuery(final String customerIdentifier, final String accessKey) {
	queueAuthorizationQuery(customerIdentifier, accessKey, null);
    }

    public void queueAuthorizationQuery(final String customerIdentifier, final String accessKey,
	    final InvoiceSubmissionMessage invoiceSubmission) {

	if (customerIdentifier == null || accessKey == null) {
	    throw new IllegalArgumentException("Mail message must be specified");
	}

	Connection connection = null;
	try {
	    connection = connectionFactory.createConnection();
	    final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	    final MessageProducer producer = session.createProducer(queryAuthQueue);
	    final ObjectMessage jmsMessage = session.createObjectMessage(new QueryAuthorizationMessage(
		    customerIdentifier, accessKey, invoiceSubmission));
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
