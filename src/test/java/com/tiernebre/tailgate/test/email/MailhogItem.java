package com.tiernebre.tailgate.test.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MailhogItem {
    @JsonProperty(value = "ID")
    private String id;
}
