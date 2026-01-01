package org.example.authserver.configuration;

import feign.RequestInterceptor;
import org.example.authserver.service.ServiceTokenManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor serviceAuthInterceptor(
            ServiceTokenManager tokenManager) {

        return requestTemplate -> {
            requestTemplate.header(
                    "Authorization",
                    "Bearer " + tokenManager.getToken()
            );
        };
    }
}
