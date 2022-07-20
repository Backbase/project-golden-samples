package org.quarkus.openapi.generator;

import java.util.Objects;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.quarkus.openapi.generator.authenticator.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigUtils {
    private static final Logger log = LoggerFactory.getLogger(ConfigUtils.class);

    private ConfigUtils() {
    }

    private static final String DEFAULT_BACKBASE_SCOPE = "backbase";
    private static final String REALM_SCOPE = "realm";

    /**
     * Method to get realm properties.
     *
     * @return {@link Config.Scope}
     */
    public static Config.Scope getRealmScope(KeycloakSession keycloakSession) {
        if (Objects.isNull(keycloakSession)) {
            return Config.scope();
        }

        return Config.scope(Utils.getPropertyWithPrefix(REALM_SCOPE, Utils.getRealmId(keycloakSession)));
    }

    /**
     * Mehtod to get backbase global properties.
     *
     * @return {@link Config.Scope}
     */
    public static Config.Scope getGlobalScope() {
        return Config.scope(DEFAULT_BACKBASE_SCOPE);
    }

    /**
     * Returns CDN URL based on realmId. As the Identity service separates realms by tenants
     * - the method is applied for cases when realmId is equal to tenantId.
     *
     * @return CDN URL.
     */
    public static String getCdnUrl(String realmId) {
        Config.Scope scope = getGlobalScope();
        try {
            return scope.get("tenants." + realmId + ".cdnUrl");
        } catch (Exception e) {
            log.error("Property for tenant {} is not configured", realmId);
            return "";
        }
    }
}
