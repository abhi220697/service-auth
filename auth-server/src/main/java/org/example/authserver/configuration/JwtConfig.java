package org.example.authserver.configuration;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwtConfig {

    @Bean
    public JwtEncoder jwtEncoder(PrivateKey privateKey) {

        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) null)
                .privateKey(privateKey)
                .keyID("auth-key")
                .build();

        JWKSource<SecurityContext> jwkSource =
                new ImmutableJWKSet<>(new JWKSet(rsaKey));

        return new NimbusJwtEncoder(jwkSource);
    }

//
//    @Bean
//    JwtEncoder jwtEncoder(RSAKey rsaKey) {
//        JWKSource<SecurityContext> jwkSource =
//                new ImmutableJWKSet<>(new JWKSet(rsaKey));
//        return new NimbusJwtEncoder(jwkSource);
//    }
}
