package com.obiectumclaro.factronica.core.mail;

import com.obiectumclaro.factronica.core.mail.model.MailMessage;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;


@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = SMTPMailServiceConstants.JNDI_BIND_NAME_SMTP_QUEUE)
})
public class SMTPMessageConsumer implements MessageListener {


    @EJB
    private SMTPMailService mailService;


    @Override
    public void onMessage(final Message message) {

        final ObjectMessage objectMessage;
        try {
            objectMessage = ObjectMessage.class.cast(message);
        } catch (final ClassCastException cce) {
            throw new RuntimeException("Incorrect message type sent to object message consumer; got:"
                    + message.getClass().getSimpleName(), cce);
        }
        final MailMessage mailMessage;
        try {
            final Object obj = objectMessage.getObject();
            mailMessage = MailMessage.class.cast(obj);
        } catch (final JMSException jmse) {
            throw new RuntimeException("Could not unwrap JMS Message", jmse);
        } catch (final ClassCastException cce) {
            throw new RuntimeException("Expected message contents of type "
                    + MailMessage.class.getSimpleName(), cce);
        }

        mailService.sendMail(mailMessage);

    }
}
