package com.obiectumclaro.factronica.core.mail;

import com.obiectumclaro.factronica.core.mail.model.Attachment;
import com.obiectumclaro.factronica.core.mail.model.MailMessage;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


public class MailMessageBuilder implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String[] EMPTY = new String[]{};
    private String from;
    private String subject;
    private String body;
    private String contentType;
    private final Collection<String> toAddresses = new HashSet<String>();
    private Boolean hasAttachment;
    private List<Attachment> attachments;

    public MailMessage build() throws IllegalStateException {

        // Validate
        if (from == null || from.length() == 0) {
            throw new IllegalStateException("from address must be specified");
        }
        if (toAddresses.size() == 0) {
            throw new IllegalStateException("at least one to address must be specified");
        }
        if (subject == null || subject.length() == 0) {
            throw new IllegalStateException("subject must be specified");
        }
        if (body == null || body.length() == 0) {
            throw new IllegalStateException("body must be specified");
        }
        if (contentType == null || contentType.length() == 0) {
            throw new IllegalStateException("contentType must be specified");
        }
        if (hasAttachment == null) {
            throw new IllegalStateException("contentType must be specified");
        }
        if (hasAttachment) {
            if (attachments == null || attachments.size() == 0) {
                throw new IllegalArgumentException("attachments must be specified");
            }

        }

        // Construct immutable object and return
        return new MailMessage(from, toAddresses.toArray(EMPTY), subject, body, contentType, hasAttachment, attachments);

    }


    public MailMessageBuilder from(final String from) throws IllegalArgumentException {
        if (from == null || from.length() == 0) {
            throw new IllegalArgumentException("from address must be specified");
        }
        this.from = from;
        return this;
    }

    public MailMessageBuilder subject(final String subject) throws IllegalArgumentException {
        if (subject == null || subject.length() == 0) {
            throw new IllegalArgumentException("subject must be specified");
        }
        this.subject = subject;
        return this;
    }

    public MailMessageBuilder body(final String body) throws IllegalArgumentException {
        if (body == null || body.length() == 0) {
            throw new IllegalArgumentException("body must be specified");
        }
        this.body = body;
        return this;
    }

    public MailMessageBuilder contentType(final String contentType) throws IllegalArgumentException {
        if (contentType == null || contentType.length() == 0) {
            throw new IllegalArgumentException("contentType must be specified");
        }
        this.contentType = contentType;
        return this;
    }

    public MailMessageBuilder addTo(final String to) {
        if (to == null || to.length() == 0) {
            throw new IllegalArgumentException("to address must be specified");
        }
        toAddresses.add(to);
        return this;
    }

    public MailMessageBuilder hasAttachment(final Boolean hasAttachment) {
        if (hasAttachment == null) {
            throw new IllegalArgumentException("hasAttachment must be specified");
        }
        this.hasAttachment = hasAttachment;
        return this;
    }

    public MailMessageBuilder attachments(final List<Attachment> attachments) {
        if (attachments == null || attachments.size() == 0) {
            throw new IllegalArgumentException("attachment must be specified");
        }
        this.attachments = attachments;
        return this;
    }


}
