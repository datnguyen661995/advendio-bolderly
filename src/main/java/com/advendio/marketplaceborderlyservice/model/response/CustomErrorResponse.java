/* (C)2022 */
package com.advendio.marketplaceborderlyservice.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomErrorResponse {
    private String errorCode;
    private String errorMsg;

    public CustomErrorResponse(String message) {
        super();
        this.errorCode = null;
        this.errorMsg = message;
    }
}
