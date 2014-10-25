package com.obiectumclaro.factronica.core.mail;

import com.obiectumclaro.factronica.core.mail.model.MailMessage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TestMessageBuilder {

    @Test
    public void propsSet() {
        final MailMessage message = new MailMessageBuilder()
                .from("from")
                .addTo("to")
                .subject("subject")
                .body("body")
                .contentType("contentType")
                .hasAttachment(false).build();
        assertEquals("from incorrect", "from", message.getFrom());
        assertEquals("to incorrect", "to", message.getTo()[0]);
        assertEquals("subject incorrect", "subject", message.getSubject());
        assertEquals("body incorrect", "body", message.getBody());
        assertEquals("contentType incorrect", "contentType", message.getContentType());
    }

    @Test(expected = IllegalStateException.class)
    public void fromAddressRequired() {
        new MailMessageBuilder().addTo("to").subject("subject").body("body").contentType("contentType").build();
    }

    @Test(expected = IllegalStateException.class)
    public void toAddressRequired() {
        new MailMessageBuilder().from("from").subject("subject").body("body").contentType("contentType").build();
    }

    @Test(expected = IllegalStateException.class)
    public void subjectRequired() {
        new MailMessageBuilder().addTo("to").from("from").body("body").contentType("contentType").build();
    }

    @Test(expected = IllegalStateException.class)
    public void bodyRequired() {
        new MailMessageBuilder().from("from").addTo("to").subject("subject").contentType("contentType").build();
    }

    @Test(expected = IllegalStateException.class)
    public void contentTypeRequired() {
        new MailMessageBuilder().from("from").addTo("to").subject("subject").body("body").build();
    }

}
