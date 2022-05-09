/* (C)2022 */
package com.advendio.marketplace.openservice.model.request;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SForceClientRequest {
    private String grant_type;
    private String assertion;
}
