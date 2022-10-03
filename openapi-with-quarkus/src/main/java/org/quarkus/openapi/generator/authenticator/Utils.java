package org.quarkus.openapi.generator.authenticator;

import java.util.Objects;
import org.eclipse.microprofile.config.ConfigProvider;
import org.keycloak.models.KeycloakSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Utils {

    private static final Logger log = LoggerFactory.getLogger(Utils.class);
    private static final String WARN_MESSAGE_NOT_DEFINED_PROPERTY =
        "Property {} is not defined; Default value `{}` is used";

    /**
     * Retrieve configuration property by name. Default value is <code>null</code>
     */
    public static String retrieveConfigProperty(String propertyName) {
        return retrieveConfigProperty(propertyName, null);
    }

    /**
     * Retrieve configuration property by name. If property is not defined default value will be used.
     */
    public static String retrieveConfigProperty(String propertyName, String defaultValue) {
        try {
            String value = ConfigProvider.getConfig().getValue(propertyName, String.class);
            if (Objects.isNull(value)) {
                log.warn(WARN_MESSAGE_NOT_DEFINED_PROPERTY, propertyName, defaultValue);
                return defaultValue;
            }
            return value;
        } catch (Exception e) {
            log.warn(WARN_MESSAGE_NOT_DEFINED_PROPERTY, propertyName, defaultValue);
            return defaultValue;
        }
    }

    public static String getRealmId(KeycloakSession keycloakSession) {
        return keycloakSession.getContext().getRealm().getId();
    }
    private Utils() {}

}
