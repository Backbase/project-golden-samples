package org.quarkus.openapi.generator;

import static org.quarkus.openapi.generator.authenticator.Utils.retrieveConfigProperty;

import com.backbase.identity.m10y.configuration.TenantConfigurationParser;
import com.backbase.identity.m10y.models.Tenant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.eclipse.microprofile.config.ConfigProvider;
import org.keycloak.Config;

public class TenantConfigurationUtils {

    private TenantConfigurationUtils() {
    }

    private static final String PROP_KEY_BACKBASE_MULTITENANCY_ENABLED = "backbase.multitenancy.enabled";

    /**
     * Get map Tenant per RealmId.
     *
     * @return Map of tenants.
     */
    public static Map<String, Tenant> getTenantForRealmIds() {
        return (new TenantConfigurationParser(ConfigProvider.getConfig(),
            Config.scope("backbase.providers.tenant-resolver.tenants"))).getConfig();
    }

    /**
     * Returns existing tenants.
     *
     * @return existing tenants
     */
    public static List<String> getTenants() {
        if (TenantConfigurationUtils.isMultiTenant()) {
            return Optional.ofNullable(TenantConfigurationUtils.getTenantForRealmIds()).orElseGet(HashMap::new)
                .values().stream().map(Tenant::getId).distinct().collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * Check if application is in multi-tenant mode.
     *
     * @return true if multi-tenant, false otherwise.
     */
    public static boolean isMultiTenant() {
        return Optional.ofNullable(retrieveConfigProperty(PROP_KEY_BACKBASE_MULTITENANCY_ENABLED, null))
            .map(Boolean::parseBoolean).orElse(false);
    }

}
