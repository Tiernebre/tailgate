package com.tiernebre.zone_blitz.mail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "zone-blitz.email")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class ZoneBlitzEmailConfigurationProperties {
    private final String from;
    private final String host;
    private final Integer port;
}
