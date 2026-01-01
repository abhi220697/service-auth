package org.example.authserver.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Configuration
public class RsaKeyConfig {

    private final JwtProperties jwtProperties;

    public RsaKeyConfig(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Bean
    public PrivateKey privateKey() throws Exception {

        Resource resource =
                new ClassPathResource(jwtProperties.getPrivateKeyLocation()
                        .replace("classpath:", ""));

        String key = new String(resource.getInputStream().readAllBytes());

        key = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decoded = Base64.getDecoder().decode(key);

        PKCS8EncodedKeySpec keySpec =
                new PKCS8EncodedKeySpec(decoded);

        return KeyFactory.getInstance("RSA")
                .generatePrivate(keySpec);
    }
}

