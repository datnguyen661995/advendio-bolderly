package com.advendio.marketplaceborderlyservice.client;

import com.advendio.marketplaceborderlyservice.client.config.AwsCognitoClientConfig;
import com.advendio.marketplaceborderlyservice.model.dto.TokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "awsAuth",url = "${spring.security.oauth2.client.provider.token-uri}", configuration = AwsCognitoClientConfig.class)
public interface AuthClient {
    @PostMapping
    TokenDto getToken(@RequestBody Map<String, String> body);
}
