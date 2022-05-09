/* (C)2022 */
package com.advendio.marketplace.openservice.service.impl;

import com.advendio.marketplace.openservice.constants.CommonConstants;
import com.advendio.marketplace.openservice.constants.SFConstant;
import com.advendio.marketplace.openservice.enums.ErrorCode;
import com.advendio.marketplace.openservice.exception.BusinessException;
import com.advendio.marketplace.openservice.model.dto.OAuthConnectionDto;
import com.advendio.marketplace.openservice.model.request.DataRequest;
import com.advendio.marketplace.openservice.service.SForceService;
import com.advendio.marketplace.openservice.sforce.BULKSForcesService;
import com.advendio.marketplace.openservice.sforce.OAuthAccessToken;
import com.advendio.marketplace.openservice.sforce.OAuthJWTBearerToken;
import com.advendio.marketplace.openservice.sforce.SOAPSForcesService;
import com.advendio.marketplace.openservice.utils.AppUtils;
import com.sforce.async.AsyncApiException;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.ws.ConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SForceServiceImpl implements SForceService {
    @Autowired Environment env;
    @Autowired private OAuthAccessToken oauthAccessToken;
    @Autowired private SOAPSForcesService soapsForcesService;
    @Autowired private BULKSForcesService bulksForcesService;

    @Override
    public OAuthConnectionDto connectToDestination(
            String userName, String endPoint, String connnectionType) {
        return connectToDestination(userName, endPoint, connnectionType, false);
    }

    @Override
    public OAuthConnectionDto connectToDestination(
            String userName, String endPoint, String connnectionType, boolean isForceRefreshToken) {
        String accessToken = getJWTAccessToken(userName, endPoint, isForceRefreshToken);
        endPoint += env.getProperty(CommonConstants.CONFIG_OAUTH_CONECTER_URL);
        return getConnectionBySessionID(userName, accessToken, endPoint, connnectionType);
    }

    @Override
    public OAuthConnectionDto getConnectionBySessionID(
            String userName, String sessionID, String endPoint, String connectionType) {
        if (SFConstant.BULK_CONNECTION.equals(connectionType)) {
            return bulksForcesService.getConnectionBySessionID(userName, sessionID, endPoint);
        } else if (SFConstant.SOAP_CONNECTION.equals(connectionType)) {
            return soapsForcesService.getConnectionBySessionID(userName, sessionID, endPoint);
        } else {
            return null;
        }
    }

    @Override
    public OAuthConnectionDto getAllConnection(
            String userName, String endPoint, String connnectionType) throws AsyncApiException {
        // create oauth connection
        OAuthConnectionDto oAuthConnectionDto =
                connectToDestination(userName, endPoint, connnectionType);
        // Create Bulk connection
        OAuthConnectionDto bulkConnectionDto =
                getConnectionForRetrieveData(
                        oAuthConnectionDto.getConfig().getServiceEndpoint(),
                        oAuthConnectionDto.getConfig().getSessionId(),
                        connnectionType);
        oAuthConnectionDto.setBulkConnection(bulkConnectionDto.getBulkConnection());
        // Verify session
        if (StringUtils.isNotBlank(oAuthConnectionDto.getConnection().getConfig().getSessionId())
                && StringUtils.isNotBlank(
                        oAuthConnectionDto.getBulkConnection().getConfig().getSessionId())) {
            return oAuthConnectionDto;
        }
        throw new BusinessException(
                HttpStatus.UNAUTHORIZED, ErrorCode.OAUTH_CONNECTION_FAILED.getMessage());
    }

    @Override
    public OAuthConnectionDto getConnectionForRetrieveData(
            String endPoint, String sessionID, String connnectionType) throws AsyncApiException {
        if (SFConstant.BULK_CONNECTION.equals(connnectionType)) {
            return bulksForcesService.getConnectionForRetrieveData(endPoint, sessionID);
        } else if (SFConstant.SOAP_CONNECTION.equals(connnectionType)) {
            return soapsForcesService.getConnectionForRetrieveData(endPoint, sessionID);
        } else {
            return null;
        }
    }

    @Override
    public String getJWTAccessToken(String userName, String endPoint, boolean isForceRefreshToken) {
        String clientID = env.getProperty(CommonConstants.CONFIG_CLIENT);
        return getJWTAccessToken(userName, endPoint, clientID, isForceRefreshToken);
    }

    @Override
    public String getJWTAccessToken(
            String userName, String endPoint, String clientID, boolean isForceRefreshToken) {
        String accessToken = "";
        try {
            Boolean sandbox =
                    env.getProperty(CommonConstants.CONFIG_SANDBOX).equals("true") ? true : false;
            String keystoreAlias = env.getProperty(CommonConstants.CONFIG_KEYSTORE_ALIAS);
            String keystorePass = env.getProperty(CommonConstants.CONFIG_KEYSTORE_PASSWORD);
            String keystorePath = env.getProperty(CommonConstants.CONFIG_KEYSTORE_PATH);

            // Step 1: get access token
            OAuthJWTBearerToken bearerTokenGenerator =
                    new OAuthJWTBearerToken(
                            keystorePath, keystorePass, keystoreAlias, keystorePass, clientID, "");
            if (isForceRefreshToken) {
                accessToken =
                        oauthAccessToken.getRefreshAccessToken(
                                userName, sandbox, bearerTokenGenerator, clientID, endPoint);
            } else {
                accessToken =
                        oauthAccessToken.getAccessToken(
                                userName, sandbox, bearerTokenGenerator, clientID, endPoint);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            AppUtils.throwBusinessException(
                    e, HttpStatus.BAD_REQUEST, ErrorCode.ERROR_CREATE_JWT_ACCESS_TOKEN);
        }
        return accessToken;
    }

    @Override
    public QueryResult query(DataRequest request, String connnectionType, String soqlQuery)
            throws AsyncApiException, ConnectionException {
        return bulksForcesService.query(
                getAllConnection(request.getUsername(), request.getEndpoint(), connnectionType),
                soqlQuery);
    }
}
