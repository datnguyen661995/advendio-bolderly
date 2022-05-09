/* (C)2022 */
package com.advendio.marketplace.openservice.service;

import com.advendio.marketplace.openservice.model.request.CreateClientRequest;
import com.advendio.marketplace.openservice.model.response.EncryptedData;

public interface EncryptionService {
    CreateClientRequest decryptCreateData(EncryptedData encryptedData);

    EncryptedData encryptData(Object request);
}
