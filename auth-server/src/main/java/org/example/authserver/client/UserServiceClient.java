package org.example.authserver.client;

import controller.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "user-service",
        url = "${user.service.url}"
)
public interface UserServiceClient extends UserApi {
}
