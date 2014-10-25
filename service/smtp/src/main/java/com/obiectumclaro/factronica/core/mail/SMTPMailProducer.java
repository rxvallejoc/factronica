package com.obiectumclaro.factronica.core.mail;

import com.obiectumclaro.factronica.core.mail.model.MailMessage;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.*;


@Stateless
@LocalBean
public class SMTPMailProducer {

    @Resource(lookup = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = SMTPMailServiceConstants.JNDI_BIND_NAME_SMTP_QUEUE)
    private Queue smtpQueue;


    public void queueMailForDelivery(final MailMessage mailMessage)
            throws IllegalArgumentException {

        if (mailMessage == null) {
            throw new IllegalArgumentException("Mail message must be specified");
        }

        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            final javax.jms.Session session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            final MessageProducer producer = session.createProducer(smtpQueue);
            final ObjectMessage jmsMessage = session.createObjectMessage(mailMessage);
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
