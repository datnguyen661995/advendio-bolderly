/* (C)2022 */
package com.advendio.marketplaceborderlyservice.model.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientRequest {
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String scope;
}
