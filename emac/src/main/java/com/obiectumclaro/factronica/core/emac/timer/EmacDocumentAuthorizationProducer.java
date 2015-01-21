package com.obiectumclaro.factronica.core.emac.timer;


import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;


@Stateless
public class EmacDocumentAuthorizationProducer {

    @Resource(lookup = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/jms/queue/EmacDocumentAuthorization")
    private Queue queryAuthQueue;

    public void queueAuthorizationQuery(final EmacDocumentSubmissionMessage document) {

        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            final MessageProducer producer = session.createProducer(queryAuthQueue);
            final ObjectMessage message = session.createObjectMessage(document);
            producer.send(message);
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
