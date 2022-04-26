package com.advendio.marketplaceborderlyservice.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientRequest {
    private String clientId;
    private String clientSecret;
    private String grantType;
}
