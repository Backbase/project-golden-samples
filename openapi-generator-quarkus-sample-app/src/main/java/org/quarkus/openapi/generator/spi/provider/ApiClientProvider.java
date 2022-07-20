package org.quarkus.openapi.generator.spi.provider;

import org.keycloak.provider.Provider;
import org.quarkus.openapi.generator.config.TodoApiConfig;

public class ApiClientProvider implements Provider {

    private final TodoApiConfig apiConfig;

    public ApiClientProvider(TodoApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    public TodoApiConfig getApiConfig() {
        return apiConfig;
    }

    @Override
    public void close() {
    }
}
