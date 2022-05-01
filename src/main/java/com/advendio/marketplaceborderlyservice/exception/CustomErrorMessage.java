package com.advendio.marketplaceborderlyservice.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access =  AccessLevel.PRIVATE)
public class CustomErrorMessage {
    public static final String ERROR_500_NAME = "Error occurred!";
    public static final String ERROR_API_TIME_OUT_NAME = "Call API TimeOut! - %s";

    public static final String ERROR_WHILE_PARSE_CLIENT_ERROR = "Error while parse client error";
    public static final String ERROR_WHILE_REQUEST_TO_ERROR = "Error while request to {} - error {}";

}
