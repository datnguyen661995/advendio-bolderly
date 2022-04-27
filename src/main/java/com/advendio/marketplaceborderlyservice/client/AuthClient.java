package com.advendio.marketplaceborderlyservice.client;

import com.advendio.marketplaceborderlyservice.client.config.AwsCognitoClientConfig;
import com.advendio.marketplaceborderlyservice.model.dto.TokenDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(value = "awsAuth",url = "${spring.security.oauth2.client.provider.token-uri}", configuration = AwsCognitoClientConfig.class)
public interface AuthClient {
    @PostMapping
    ResponseEntity<Map<String, Object>> getToken(@RequestHeader Map<String, String> headerMap);
}
