package com.tiernebre.tailgate.test.email.mailhog;

import lombok.Data;

import java.util.List;

@Data
public class MailhogSearchResponse {
    private Integer total;
    private Integer start;
    private Integer count;
    private List<MailhogItem> items;
}
