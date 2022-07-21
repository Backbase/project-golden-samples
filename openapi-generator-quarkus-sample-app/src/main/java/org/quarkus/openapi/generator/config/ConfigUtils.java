package org.quarkus.openapi.generator.config;

import static org.quarkus.openapi.generator.config.api.global.GlobalConfigConstants.BACKBASE_SCOPE;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.Config;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigUtils {

    /**
     * Mehtod to get backbase global properties.
     *
     * @return {@link Config.Scope}
     */
    public static Config.Scope getGlobalScope() {
        return Config.scope(BACKBASE_SCOPE);
    }

}
