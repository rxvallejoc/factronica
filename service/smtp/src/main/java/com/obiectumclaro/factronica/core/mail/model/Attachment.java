package com.obiectumclaro.factronica.core.mail.model;

import java.io.Serializable;


public class Attachment implements Serializable {


    private static final long serialVersionUID = 1L;
    private byte[] attachment;
    private String attachmentName;
    private String attachmentContentType;


    public Attachment(byte[] attachment, String attachmentName, String attachmentContentType) {
        this.attachment = attachment;
        this.attachmentName = attachmentName;
        this.attachmentContentType = attachmentContentType;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentContentType() {
        return attachmentContentType;
    }

    public void setAttachmentContentType(String attachmentContentType) {
        this.attachmentContentType = attachmentContentType;
    }


}
