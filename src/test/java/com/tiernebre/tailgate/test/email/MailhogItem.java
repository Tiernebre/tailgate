package com.tiernebre.tailgate.test.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Set;

@Data
public class MailhogItem {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("From")
    private MailhogItemContact from;

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
}
