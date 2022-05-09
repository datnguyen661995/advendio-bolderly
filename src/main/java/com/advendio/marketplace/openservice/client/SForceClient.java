/* (C)2022 */
package com.advendio.marketplace.openservice.client;

import com.advendio.marketplace.openservice.client.config.FormUrlConfig;
import com.advendio.marketplace.openservice.model.request.SForceClientRequest;
import feign.Headers;
import feign.Param;
import java.net.URI;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        value = "sForceAuth",
        url = "${app.service.sforce.client.provider.token-uri}",
        configuration = FormUrlConfig.class)
public interface SForceClient {
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Headers({"Content-Type: application/x-www-form-urlencoded", "forwardUrl: {url}"})
    String getAccessToken(@Param("url") URI url, SForceClientRequest sForceClientRequest);
}
