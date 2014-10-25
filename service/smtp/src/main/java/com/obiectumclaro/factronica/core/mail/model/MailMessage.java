package com.obiectumclaro.factronica.core.mail.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


public class MailMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String from;
    private String[] to;
    private String subject;
    private String body;
    private String contentType;
    private Boolean hasAttachment;
    private List<Attachment> attachments;


    public MailMessage(final String from, final String[] to, final String subject, final String body, final String contentType, final Boolean hasAttachment, final List<Attachment> attachments) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.contentType = contentType;
        this.hasAttachment = hasAttachment;
        this.attachments = attachments;
    }

    public String getFrom() {
        return from;
    }

    public String[] getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public Boolean hasAttachment() {
        return hasAttachment;
    }

    public String getContentType() {
        return contentType;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    @Override
    public String toString() {
        return "MailMessage{" +
                "from='" + from + '\'' +
                ", to=" + Arrays.toString(to) +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
