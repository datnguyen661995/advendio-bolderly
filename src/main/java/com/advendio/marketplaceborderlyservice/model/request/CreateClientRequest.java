/* (C)2022 */
package com.advendio.marketplaceborderlyservice.model.request;

import java.util.List;
import lombok.Data;

@Data
public class CreateClientRequest {
    private String clientName;
    private List<String> scopes;
}
