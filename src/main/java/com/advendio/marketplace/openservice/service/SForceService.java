/* (C)2022 */
package com.advendio.marketplace.openservice.service;

import com.advendio.marketplace.openservice.model.dto.OAuthConnectionDto;
import com.advendio.marketplace.openservice.model.request.DataRequest;
import com.sforce.async.AsyncApiException;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.ws.ConnectionException;

public interface SForceService {
    OAuthConnectionDto getConnectionForRetrieveData(
            String endPoint, String sessionID, String connnectionType) throws AsyncApiException;

    OAuthConnectionDto connectToDestination(
            String userName, String endPoint, String connnectionType);

    OAuthConnectionDto connectToDestination(
            String userName, String endPoint, String connnectionType, boolean isForceRefreshToken);

    OAuthConnectionDto getConnectionBySessionID(
            String userName, String sessionID, String endPoint, String connectionType);

    OAuthConnectionDto getAllConnection(String userName, String endPoint, String connnectionType)
            throws AsyncApiException;

    String getJWTAccessToken(String userName, String endPoint, boolean isForceRefreshToken);

    String getJWTAccessToken(
            String userName, String endPoint, String clientID, boolean isForceRefreshToken);

    QueryResult query(DataRequest request, String connnectionType, String soqlQuery)
            throws AsyncApiException, ConnectionException;
}
