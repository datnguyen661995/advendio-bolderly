/* (C)2022 */
package com.advendio.marketplace.openservice.client;

import com.advendio.marketplace.openservice.client.config.FormUrlConfig;
import com.advendio.marketplace.openservice.model.dto.TokenDto;
import com.advendio.marketplace.openservice.model.request.ClientRequest;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        value = "awsAuth",
        url = "${spring.security.oauth2.client.provider.token-uri}",
        configuration = FormUrlConfig.class)
public interface AuthClient {
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Headers("Content-Type: application/x-www-form-urlencoded")
    TokenDto getToken(ClientRequest clientRequest);
}
