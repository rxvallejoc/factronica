/**
 * 
 */
package com.obiectumclaro.factronica.core.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Test;

/**
 * @author iapazmino
 * 
 */
public class JavaMailSpike {

	private Properties props;

	@Before
	public void configSMTP() {
		props = new Properties();
		props.put("mail.smtp.host", "rsj21.rhostjh.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		System.out.println("Properties read...");
	}

	@Test
	public void testMailingOverSSL() {
		
		Session session = startSession("dev-tools@obiectumclaro.com", "ICTools2012,");
		System.out.println("Session started...");

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("dev-tools@obiectumclaro.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("iapazmino@gmail.com"));
			message.setSubject("Java Mail Spike");
			message.setText("Mailing over SSL");
			System.out.println("Message composed...");

			Transport.send(message);
			System.out.println("Message sent.");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private Session startSession(final String username, final String password) {
		return Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
	}

}
