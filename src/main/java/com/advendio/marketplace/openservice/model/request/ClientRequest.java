/* (C)2022 */
package com.advendio.marketplace.openservice.model.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class ClientRequest {
    private String grant_type;
    private String client_id;
    private String client_secret;
    private List<String> scope;
}
