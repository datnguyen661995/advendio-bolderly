/* (C)2022 */
package com.advendio.marketplace.openservice.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public enum ErrorCode {
    ERROR_500_NAME("Error occurred!", "Error occurred!"),
    ERROR_API_TIME_OUT_NAME("Call API TimeOut! - %s", "Call API TimeOut! - %s"),
    ERROR_WHILE_PARSE_CLIENT_ERROR(
            "Error while parse client error", "Error while parse client error"),
    ERROR_WHILE_REQUEST_TO_ERROR(
            "Error while request to {} - error {}", "Error while request to {} - error {}"),

    COGNITO_ERROR_CLIENT_ID_IS_NOT_SUITABLE(
            "This client is prevented to get token", "This client is prevented to get token"),
    OAUTH_CONNECTION_FAILED("OAUTH_CONNECTION_FAILED", "OAuth Connection is failed"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Server Error."),
    PROPERTIES_RESOURCE_NOT_FOUND(
            "PROPERTIES_RESOURCE_NOT_FOUND", "Properties resource is not found:: %s"),
    ERROR_CREATE_JWT_ACCESS_TOKEN(
            "ERROR_CREATE_JWT_ACCESS_TOKEN", "Error create JWT access token."),
    ERROR_DATA_NOT_FOUND("ERROR_DATA_NOT_FOUND", "Data not found to sync."),
    AWS_RESOURCE_NOT_FOUND_EXCEPTION("ResourceNotFoundException", "clientId is not exist");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class Constants {
        public static final String USERNAME_REQUIRED_MESSAGE = "Please provide username";
        public static final String ENDPOINT_REQUIRED_MESSAGE = "Please provide endpoint";
        public static final String ENDPOINT_INVALID_MESSAGE =
                "Please provide endpoint valid format url https";
        public static final String RECORD_ID_INVALID_MESSAGE = "RecordID can not exceed 18 digits";
        public static final String DATA_TYPE_REQUIRED_MESSAGE = "Please provide datatype";
        public static final String DATA_TYPE_INVALID_MESSAGE = "Datatype is invalid";
        public static final String MEDIA_CAMPAIGN_RECORD_ID_WITH_DATA_TYPE_INVALID_MESSAGE =
                "Media Campaign RecordID is not support in datatype";
        public static final String MEDIA_CAMPAIGN_RECORD_ID_INVALID_MESSAGE =
                "Media Campaign RecordID can not exceed 18 digits";
        public static final String REVENUE_SCHEDULE_DAYS_WITH_DATA_TYPE_INVALID_MESSAGE =
                "Revenue Schedule Days is not support in datatype";
    }
}
