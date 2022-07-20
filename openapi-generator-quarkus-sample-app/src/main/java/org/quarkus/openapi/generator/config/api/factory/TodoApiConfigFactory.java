package org.quarkus.openapi.generator.config.api.factory;

import static org.quarkus.openapi.generator.config.global.GlobalConfigConstants.TODO_API_BASE_URL;

import com.backbase.identity.m10y.models.Tenant;
import java.util.Objects;
import java.util.Optional;
import org.quarkus.openapi.generator.config.TodoApiConfig;
import org.quarkus.openapi.todo.api.TodosApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TodoApiConfigFactory {

    private TodoApiConfigFactory() {
    }

    private static final Logger log = LoggerFactory.getLogger(TodoApiConfigFactory.class);

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
     * Global scope can be used to get base url. In this ex, we will just retrieve base url for the api from the constants
     *
     * @return {@link TodoApiConfig}
     */
    public static TodoApiConfig createTodoApiConfig(Tenant tenant) {
        Optional<String> tenantId = Optional.ofNullable(tenant).map(Tenant::getId);

        TodosApi todosApi =
            createTodoApi(tenantId.orElse(null), TODO_API_BASE_URL);

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
