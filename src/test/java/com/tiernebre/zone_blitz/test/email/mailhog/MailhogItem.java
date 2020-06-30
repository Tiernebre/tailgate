package com.tiernebre.zone_blitz.test.email.mailhog;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class MailhogItem {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("From")
    private MailhogItemContact from;
    @JsonProperty("To")
    private List<MailhogItemContact> to;
    @JsonProperty("Content")
    private MailhogItemContent content;
    @JsonProperty("Created")
    private String created;
    @JsonProperty("MIME")
    private String mime;
    @JsonProperty("Raw")
    private MailhogItemRaw raw;

    @Data
    public static class MailhogItemContact {
        @JsonProperty("Relays")
        private Set<String> relays;
        @JsonProperty("Mailbox")
        private String mailbox;
        @JsonProperty("Domain")
        private String domain;
        @JsonProperty("Params")
        private String params;
    }

    @Data
    public static class MailhogItemContent {
        @JsonProperty("Headers")
        MailhogItemContentHeaders headers;
        @JsonProperty("Body")
        private String body;
        @JsonProperty("MIME")
        private String mime;
        @JsonProperty("Size")
        private long Size;
    }

    @Data
    public static class MailhogItemContentHeaders {
        @JsonProperty("Content-Transfer-Encoding")
        private Set<String> contentTransferEncoding;
        @JsonProperty("Content-Type")
        private Set<String> contentType;
        @JsonProperty("Date")
        private Set<String> date;
        @JsonProperty("From")
        private Set<String> from;
        @JsonProperty("MIME-Version")
        private Set<String> mimeVersion;
        @JsonProperty("Message-ID")
        private Set<String> messageId;
        @JsonProperty("Received")
        private Set<String> received;
        @JsonProperty("Return-Path")
        private Set<String> returnPath;
        @JsonProperty("Subject")
        private Set<String> subject;
        @JsonProperty("To")
        private Set<String> to;
    }

    @Data
    public static class MailhogItemRaw {
        @JsonProperty("From")
        private String from;
        @JsonProperty("To")
        private Set<String> to;
        @JsonProperty("Data")
        private String data;
        @JsonProperty("Helo")
        private String helo;
    }
}
