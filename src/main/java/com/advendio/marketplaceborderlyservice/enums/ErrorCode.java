/* (C)2022 */
package com.advendio.marketplaceborderlyservice.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    ERROR_500_NAME("Error occurred!", "Error occurred!"),
    ERROR_API_TIME_OUT_NAME("Call API TimeOut! - %s", "Call API TimeOut! - %s"),
    ERROR_WHILE_PARSE_CLIENT_ERROR(
            "Error while parse client error", "Error while parse client error"),
    ERROR_WHILE_REQUEST_TO_ERROR(
            "Error while request to {} - error {}", "Error while request to {} - error {}");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
