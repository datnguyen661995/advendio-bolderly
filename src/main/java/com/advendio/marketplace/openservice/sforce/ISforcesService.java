/* (C)2022 */
package com.advendio.marketplace.openservice.sforce;

import com.advendio.marketplace.openservice.model.dto.OAuthConnectionDto;
import com.sforce.async.AsyncApiException;

public interface ISforcesService {
    OAuthConnectionDto getConnectionBySessionID(String userName, String sessionID, String endPoint);

    OAuthConnectionDto getConnectionForRetrieveData(String endPoint, String sessionID)
            throws AsyncApiException;
}
