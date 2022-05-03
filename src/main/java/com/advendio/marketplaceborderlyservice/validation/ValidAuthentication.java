/* (C)2022 */
package com.advendio.marketplaceborderlyservice.validation;

import javax.servlet.http.HttpServletRequest;

public interface ValidAuthentication {

    /**
     * * Validate Key in request
     *
     * @param request
     * @return
     */
    boolean validateKey(HttpServletRequest request);
}
