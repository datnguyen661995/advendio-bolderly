/* (C)2022 */
package com.advendio.marketplaceborderlyservice.validation;

import javax.servlet.http.HttpServletRequest;

public class KeyAuthenticationValidator implements ValidAuthentication {
    @Override
    public boolean validateKey(HttpServletRequest request) {
        return false;
    }
}
