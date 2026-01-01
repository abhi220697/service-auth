package org.example.authserver.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auth.jwt")
@Getter
@Setter
public class JwtProperties {

    private String privateKeyLocation;
    private String issuer;
    private long tokenValiditySeconds;
}
