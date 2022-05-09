/* (C)2022 */
package com.advendio.marketplace.openservice.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonConstants {
    public static final String COGNITO_GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
    public static final String CONFIG_OAUTH_VERSION = "oauth.apiVersion";
    public static final String CONFIG_SANDBOX = "sandbox";
    public static final String CONFIG_OAUTH_CONECTER_URL = "oauth.connecter.url";

    // KEY
    public static final String CONFIG_CLIENT = "clientID";
    public static final String CONFIG_KEYSTORE_ALIAS = "keystore.alias";
    public static final String CONFIG_KEYSTORE_PASSWORD = "keystore.password";
    public static final String CONFIG_KEYSTORE_PATH = "keystore.path";

    // PATTERN
    public static final String REGEX_ENDPOIND = "^(|https:\\/\\/[^ \"]+)$";
    public static final String REGEX_RECORD_ID =
            "^(|[a-zA-Z0-9]{18})$"; // accept empty || must length 18
}
