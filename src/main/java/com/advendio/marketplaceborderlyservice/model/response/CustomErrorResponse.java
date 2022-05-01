package com.advendio.marketplaceborderlyservice.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomErrorResponse {
    private String errorCode;
    private String errorMsg;

    public CustomErrorResponse(String message) {
        super();
        this.errorCode = null;
        this.errorMsg = message;
    }

    public CustomErrorResponse() {
    }

    public CustomErrorResponse(String code , String message) {
        super();
        this.errorCode = code;
        this.errorMsg = message;
    }




}
