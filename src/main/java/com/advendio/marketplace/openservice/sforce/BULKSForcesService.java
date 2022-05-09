/* (C)2022 */
package com.advendio.marketplace.openservice.sforce;

import com.advendio.marketplace.openservice.constants.CommonConstants;
import com.advendio.marketplace.openservice.enums.ErrorCode;
import com.advendio.marketplace.openservice.exception.BusinessException;
import com.advendio.marketplace.openservice.model.dto.OAuthConnectionDto;
import com.sforce.async.AsyncApiException;
import com.sforce.async.BulkConnection;
import com.sforce.soap.enterprise.Connector;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BULKSForcesService implements ISforcesService {
    @Value("${app.service.sforce.http.connectTimeout}")
    private int connectTimeout;

    @Autowired private Environment env;

    @Override
    public OAuthConnectionDto getConnectionBySessionID(
            String userName, String sessionID, String endPoint) {
        OAuthConnectionDto connectionInfo = new OAuthConnectionDto();

        ConnectorConfig config = new ConnectorConfig();
        config.setUsername(userName);
        config.setSessionId(sessionID);
        config.setServiceEndpoint(endPoint);

        try {
            EnterpriseConnection connection = Connector.newConnection(config);
            // display some current settings
            log.debug("Auth EndPoint={}", config.getAuthEndpoint());
            log.debug("Service EndPoint={}", config.getServiceEndpoint());
            log.debug("Username={}", config.getUsername());

            connectionInfo.setConfig(config);
            connectionInfo.setConnection(connection);

            return connectionInfo;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new BusinessException(ErrorCode.OAUTH_CONNECTION_FAILED.getCode(), ex.toString());
        }
    }

    @Override
    public OAuthConnectionDto getConnectionForRetrieveData(String endPoint, String sessionID)
            throws AsyncApiException {
        ConnectorConfig config = new ConnectorConfig();
        config.setSessionId(sessionID);
        // The endpoint for the Bulk API service is the same as for the normal
        // SOAP uri until the /Soap/ part. From here it's '/async/versionNumber'
        String soapEndpoint = endPoint;
        if (!soapEndpoint.endsWith("/")) {
            soapEndpoint += "/";
        }
        String apiVersion = env.getProperty(CommonConstants.CONFIG_OAUTH_VERSION);
        String restEndpoint =
                soapEndpoint.substring(0, soapEndpoint.indexOf("Soap/")) + "async/" + apiVersion;
        config.setRestEndpoint(restEndpoint);
        // This should only be false when doing debugging.
        config.setCompression(true);
        // Set this to true to see HTTP requests and responses on stdout
        config.setTraceMessage(false);
        config.setConnectionTimeout(connectTimeout);
        BulkConnection connection = new BulkConnection(config);
        OAuthConnectionDto authConnectionDto = new OAuthConnectionDto();
        authConnectionDto.setBulkConnection(connection);
        authConnectionDto.setConfig(config);
        return authConnectionDto;
    }

    public QueryResult query(OAuthConnectionDto authConnectionDto, String soqlQuery)
            throws ConnectionException {
        return authConnectionDto.getConnection().query(soqlQuery);
    }
}
