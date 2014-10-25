package com.obiectumclaro.factronica.core.mail;

import com.obiectumclaro.factronica.core.mail.model.Attachment;
import com.obiectumclaro.factronica.core.mail.model.MailMessage;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

@Stateless
@LocalBean
public class SMTPMailService {

    @Resource(lookup = SMTPMailServiceConstants.JNDI_BIND_NAME_MAIL_SESSION)
    private Session mailSession;

    public void sendMail(final MailMessage mailMessage) {
        if (mailMessage == null) {
            throw new IllegalArgumentException("Mail message must be specified");
        }
        try {
            final MimeMessage mimeMessage = new MimeMessage(mailSession);
            final Address from = new InternetAddress(mailMessage.getFrom());
            final int numToAddresses = mailMessage.getTo().length;
            final Address[] to = new InternetAddress[numToAddresses];
            String[] mailTo = mailMessage.getTo();
            for (int i = 0; i < numToAddresses; i++) {
                to[i] = new InternetAddress(mailTo[i]);
            }
            mimeMessage.setFrom(from);
            mimeMessage.setRecipients(Message.RecipientType.TO, to);
            mimeMessage.setSubject(mailMessage.getSubject());

            // create MimeBodyPart object and set your message content
            if (mailMessage.hasAttachment()) {
                BodyPart messageBodyText = new MimeBodyPart();
                messageBodyText.setText(mailMessage.getBody());

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyText);

                for (Attachment attachment : mailMessage.getAttachments()) {
                    MimeBodyPart messageBodyAttachment = new MimeBodyPart();
                    DataSource source = new ByteArrayDataSource(attachment.getAttachment(),
                            attachment.getAttachmentContentType());
                    messageBodyAttachment.setDataHandler(new DataHandler(source));
                    messageBodyAttachment.setFileName(attachment.getAttachmentName());

                    multipart.addBodyPart(messageBodyAttachment);
                }

                mimeMessage.setContent(multipart);

            } else {
                mimeMessage.setContent(mailMessage.getBody(), mailMessage.getContentType());
            }

            Transport.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Error in sending " + mailMessage, e);
        }
    }

}
