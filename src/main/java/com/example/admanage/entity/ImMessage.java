package com.example.admanage.entity;

import java.io.Serializable;

/**
 * @author weili12
 * @Des 消息
 */
public class ImMessage implements Serializable{
    /**
     * 消息id
     */
    private String messageId;
    /**
     * 消息类型 文本、文件
     */
    private String messageType;

    /**
     * 消息内容
     */
    private String messageContent;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
