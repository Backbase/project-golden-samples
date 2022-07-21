package org.quarkus.openapi.generator.spi.factory;

import com.backbase.identity.m10y.models.Tenant;
import com.backbase.identity.m10y.providers.TenantResolverProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.quarkus.openapi.generator.TenantConfigurationUtils;
import org.quarkus.openapi.generator.authenticator.Utils;
import org.quarkus.openapi.generator.config.TodoApiConfig;
import org.quarkus.openapi.generator.api.factory.TodoApiConfigFactory;
import org.quarkus.openapi.generator.spi.provider.ApiClientProvider;

public class TenantAwareApiClientFactory implements ApiClientProviderFactory {

    private Map<String, TodoApiConfig> tenantsApiConfigMap = new HashMap<>();
    private TodoApiConfig singleTenantApiConfig;

    @Override
    public ApiClientProvider create(KeycloakSession keycloakSession) {
        TenantResolverProvider provider = keycloakSession.getProvider(TenantResolverProvider.class);
        TodoApiConfig apiConfig = provider.resolveTenant(Utils.getRealmId(keycloakSession))
            .map(t -> tenantsApiConfigMap.get(t.getId()))
            .orElse(singleTenantApiConfig);

        return new ApiClientProvider(apiConfig);
    }

    @Override
    public void init(Config.Scope scope) {
        singleTenantApiConfig = TodoApiConfigFactory.createTodoApiConfig();
        tenantsApiConfigMap = createMultiTenantApiConfigs();
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return "tenant-aware-api-client-provider";
    }

    private Map<String, TodoApiConfig> createMultiTenantApiConfigs() {
        Map<String, Tenant> tenantsPerRealm = TenantConfigurationUtils.getTenantForRealmIds();
        if (Objects.isNull(tenantsPerRealm)) {
            return new HashMap<>();
        }

        return tenantsPerRealm.values()
            .stream()
            .distinct()
            .filter(Objects::nonNull)
            .map(t -> new ApiConfigWithTenant(t.getId(), TodoApiConfigFactory.createTodoApiConfig(t)))
            .filter(c -> Objects.nonNull(c.todoApiConfig) && Objects.nonNull(c.tenantId))
            .collect(Collectors.toMap(c -> c.tenantId, c -> c.todoApiConfig));
    }

    private static class ApiConfigWithTenant {
        String tenantId;
        TodoApiConfig todoApiConfig;

        public ApiConfigWithTenant(String tenantId, TodoApiConfig todoApiConfig) {
            this.tenantId = tenantId;
            this.todoApiConfig = todoApiConfig;
        }

    }

}
