package org.quarkus.openapi.generator.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.quarkus.openapi.generator.config.api.factory.ApiClientFactory;
import org.quarkus.openapi.todo.api.ApiClient;

class ApiClientFactoryTest {

    @Test
    void shouldCreateApiClient() {
        String basePath = "path";

        ApiClient traceApiClient = ApiClientFactory.createTraceApiClient(basePath);

        assertNotNull(traceApiClient);
        assertEquals(basePath, traceApiClient.getBasePath());
    }

    @Test
    void shouldCreateTenantAwareApiClient() {
        String basePath = "path";
        String tenantId = "tenantId";

        ApiClient traceApiClient = ApiClientFactory.createTenantApiClient(tenantId, basePath);

        assertNotNull(traceApiClient);
        assertEquals(basePath, traceApiClient.getBasePath());
    }

    @Test
    void shouldCreateApiClientWithoutTenantId() {
        String basePath = "path";

        ApiClient traceApiClient = ApiClientFactory.createTenantApiClient(null, basePath);

        assertNotNull(traceApiClient);
        assertEquals(basePath, traceApiClient.getBasePath());
    }

}