/* (C)2022 */
package com.advendio.marketplace.openservice.model.request;

import com.advendio.marketplace.openservice.enums.ErrorCode;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DataRequest {
    @NotEmpty(message = ErrorCode.Constants.USERNAME_REQUIRED_MESSAGE)
    private String username;

    //    @NotEmpty(message = ErrorCode.Constants.ENDPOINT_REQUIRED_MESSAGE)
    //    @Pattern(regexp = CommonConstants.REGEX_ENDPOIND, message =
    // ErrorCode.Constants.ENDPOINT_INVALID_MESSAGE)
    private String endpoint;

    //    @NotEmpty(message = ErrorCode.Constants.DATA_TYPE_REQUIRED_MESSAGE)
    //    @ValidEnum(enumClass = DataTypeEnum.class, ignoreCase = true, message =
    // ErrorCode.Constants.DATA_TYPE_INVALID_MESSAGE)
    private String datatype;

    //    @Pattern(regexp = CommonConstants.REGEX_RECORD_ID, message =
    // ErrorCode.Constants.RECORD_ID_INVALID_MESSAGE)
    private String recordid;

    //    @Pattern(regexp = CommonConstants.REGEX_RECORD_ID, message =
    // ErrorCode.Constants.MEDIA_CAMPAIGN_RECORD_ID_INVALID_MESSAGE)
    private String mediaCampaignRecordId;

    // optional: support for type 'DeliveryData'
    @Min(0)
    private Integer revenueScheduleDays;
}
