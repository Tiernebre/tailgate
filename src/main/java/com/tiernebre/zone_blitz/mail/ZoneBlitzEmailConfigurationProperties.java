package com.tiernebre.zone_blitz.mail;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "zone-blitz.email")
@ConstructorBinding
@Value
public class ZoneBlitzEmailConfigurationProperties {
    String from;
    String host;
    Integer port;
}
