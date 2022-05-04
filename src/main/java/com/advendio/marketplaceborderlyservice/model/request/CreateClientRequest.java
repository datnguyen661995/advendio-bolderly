package com.advendio.marketplaceborderlyservice.model.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateClientRequest {
    private String clientName;
    private List<String> scopes;
}
