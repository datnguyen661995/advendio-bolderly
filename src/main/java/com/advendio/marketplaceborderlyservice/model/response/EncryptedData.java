package com.advendio.marketplaceborderlyservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncryptedData {
    private byte[] data;
    private byte[] key;
}
