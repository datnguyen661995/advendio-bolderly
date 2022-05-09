/* (C)2022 */
package com.advendio.marketplace.openservice.model.dto;

import com.sforce.async.BulkConnection;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.ws.ConnectorConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuthConnectionDto {
    private ConnectorConfig config;
    private EnterpriseConnection connection;
    private BulkConnection bulkConnection;
};
