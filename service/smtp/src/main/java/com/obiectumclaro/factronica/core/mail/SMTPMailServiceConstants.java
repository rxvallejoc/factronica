package com.obiectumclaro.factronica.core.mail;


public interface SMTPMailServiceConstants {

    String JNDI_BIND_NAME_MAIL_SESSION = "java:jboss/mail/FactronicaSMTP";

    String JNDI_BIND_NAME_SMTP_QUEUE = "java:/jms/queue/FactronicaSMTP";
}
