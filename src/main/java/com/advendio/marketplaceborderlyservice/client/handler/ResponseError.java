/* (C)2022 */
package com.advendio.marketplaceborderlyservice.client.handler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseError {
    private String errorCode;
    private String errorMessage;
    private String message;

    @JsonProperty(value = "DATA")
    private Data data;

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Accessors(chain = true)
    public static class Data {
        private String errorCode;
        private String errorMsg;
    }
}
