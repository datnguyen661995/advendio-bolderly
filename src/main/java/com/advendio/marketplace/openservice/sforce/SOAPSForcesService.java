/* (C)2022 */
package com.advendio.marketplace.openservice.sforce;

import com.advendio.marketplace.openservice.model.dto.OAuthConnectionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SOAPSForcesService implements ISforcesService {

    @Override
    public OAuthConnectionDto getConnectionBySessionID(
            String userName, String sessionID, String endPoint) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OAuthConnectionDto getConnectionForRetrieveData(String endPoint, String sessionID) {
        // TODO Auto-generated method stub
        return null;
    }
}
