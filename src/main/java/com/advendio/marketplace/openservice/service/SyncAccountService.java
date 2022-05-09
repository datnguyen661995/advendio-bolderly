/* (C)2022 */
package com.advendio.marketplace.openservice.service;

import com.advendio.marketplace.openservice.model.request.DataRequest;
import com.sforce.async.AsyncApiException;
import com.sforce.soap.enterprise.sobject.Account;
import java.io.IOException;
import java.util.List;

public interface SyncAccountService {
    List<Account> getAccounts(DataRequest dataRequest, String connnectionType);

    void createAccounts(
            String sobjectType, String userName, String endPoint, String connnectionType)
            throws AsyncApiException, IOException;
}
