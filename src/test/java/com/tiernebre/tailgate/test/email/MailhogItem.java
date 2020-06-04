package com.tiernebre.tailgate.test.email;

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

    @Data
    private static class MailhogItemContact {
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
    private static class MailhogItemContent {

    }

    @Data
    private static class MailhogItemContentHeaders {
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
}
