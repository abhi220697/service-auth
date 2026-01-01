package org.example.authserver.service;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class ServiceTokenManager {

    private final ServiceTokenProvider tokenProvider;

    private String cachedToken;
    private Instant expiryTime;

    public ServiceTokenManager(ServiceTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public synchronized String getToken() {

        if (cachedToken == null || Instant.now().isAfter(expiryTime)) {
            cachedToken = tokenProvider.generateServiceToken();
            expiryTime = Instant.now().plus(4, ChronoUnit.MINUTES);
        }

        return cachedToken;
    }
}
