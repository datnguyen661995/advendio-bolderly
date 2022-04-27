package com.advendio.marketplaceborderlyservice.client;

//import com.advendio.marketplaceborderlyservice.client.config.AwsCognitoClientConfig;

import com.advendio.marketplaceborderlyservice.client.config.FormUrlConfig;
import com.advendio.marketplaceborderlyservice.model.request.ClientRequest;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(value = "awsAuth", url = "${spring.security.oauth2.client.provider.token-uri}", configuration = FormUrlConfig.class)
public interface AuthClient {
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Headers("Content-Type: application/x-www-form-urlencoded")
    ResponseEntity<Map<String, Object>> getToken(ClientRequest clientRequest);
}
