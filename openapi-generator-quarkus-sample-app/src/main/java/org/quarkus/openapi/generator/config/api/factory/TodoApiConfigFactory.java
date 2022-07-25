package org.quarkus.openapi.generator.config.api.factory;

import static org.quarkus.openapi.generator.config.api.global.GlobalConfigConstants.TODO_API_BASE_URL_KEY;

import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.Config;
import org.quarkus.openapi.generator.config.ConfigUtils;
import org.quarkus.openapi.generator.config.TodoApiConfig;
import org.quarkus.openapi.generator.models.Tenant;
import org.quarkus.openapi.todo.api.TodosApi;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoApiConfigFactory {

    /**
     * Create Todo Api Config without tenant data.
     *
     * @return {@link TodoApiConfig}
     */
    public static TodoApiConfig createTodoApiConfig() {
        return createTodoApiConfig(null);
    }

    /**
     * Create Todo Api Config for a given tenant.
     * Global scope is used to get base url.
     *
     * @return {@link TodoApiConfig}
     */
    public static TodoApiConfig createTodoApiConfig(Tenant tenant) {
        Config.Scope scope = ConfigUtils.getGlobalScope();

        Optional<String> tenantId = Optional.ofNullable(tenant).map(Tenant::getId);

        TodosApi todosApi =
            createTodoApi(tenantId.orElse(null), scope.get(TODO_API_BASE_URL_KEY));

        if (Objects.isNull(todosApi)) {
            log.error("Can't initialize Todos api for tenant: {}", tenantId.orElse("<no tenant>"));
            return null;
        }

        return new TodoApiConfig(todosApi);
    }

    private static TodosApi createTodoApi(String tenantId, String baseUrl) {
        if (Objects.isNull(baseUrl)) {
            log.error("TodoApi base url is null");
            return null;
        }

        return new TodosApi(ApiClientFactory.createTenantApiClient(tenantId, baseUrl));
    }
}
