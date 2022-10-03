package org.quarkus.openapi.generator.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.Config;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quarkus.openapi.generator.config.api.factory.ApiClientFactory;
import org.quarkus.openapi.generator.config.api.factory.TodoApiConfigFactory;
import org.quarkus.openapi.generator.models.Tenant;
import org.quarkus.openapi.todo.api.ApiClient;

@ExtendWith(MockitoExtension.class)
class TodoApiConfigFactoryTest {

    @Mock
    private Config.Scope mockScope;

    @Mock
    private ApiClient mockApiClient;

    @Mock
    private Tenant mockTenant;

    @Test
    void shouldNotCreateTodoConfigWhenBasePathIsMissing() {
        try (MockedStatic<ConfigUtils> mockedConfigUtils = mockStatic(ConfigUtils.class)) {
            mockedConfigUtils.when(ConfigUtils::getGlobalScope).thenReturn(mockScope);
            when(mockScope.get(any())).thenReturn(null);

            TodoApiConfig todoApiConfig = TodoApiConfigFactory.createTodoApiConfig();

            assertNull(todoApiConfig);
        }
    }

    @Test
    void shouldCreateTodoApiConfigWithoutTenant() {
        try (MockedStatic<ConfigUtils> mockedConfigUtils = mockStatic(ConfigUtils.class)) {
            mockedConfigUtils.when(ConfigUtils::getGlobalScope).thenReturn(mockScope);
            when(mockScope.get(any())).thenReturn("basePath");

            try (MockedStatic<ApiClientFactory> mockedApiClientFactory = mockStatic(ApiClientFactory.class)) {
                mockedApiClientFactory.when(() -> ApiClientFactory.createTenantApiClient(null, "basePath"))
                    .thenReturn(mockApiClient);

                TodoApiConfig todoApiConfig = TodoApiConfigFactory.createTodoApiConfig();

                assertNotNull(todoApiConfig);
                mockedApiClientFactory.verify(() -> ApiClientFactory.createTenantApiClient(null, "basePath"), times(1));
            }
        }
    }

    @Test
    void shouldCreateTodoConfigWithTenant() {
        try (MockedStatic<ConfigUtils> mockedConfigUtils = mockStatic(ConfigUtils.class)) {
            mockedConfigUtils.when(ConfigUtils::getGlobalScope).thenReturn(mockScope);
            when(mockScope.get(any())).thenReturn("basePath");
            when(mockTenant.getId()).thenReturn("tenantId");
            try (MockedStatic<ApiClientFactory> mockedApiClientFactory = mockStatic(ApiClientFactory.class)) {
                mockedApiClientFactory.when(() -> ApiClientFactory.createTenantApiClient("tenantId", "basePath"))
                    .thenReturn(mockApiClient);

                TodoApiConfig todoApiConfig = TodoApiConfigFactory.createTodoApiConfig(mockTenant);

                assertNotNull(todoApiConfig);
                mockedApiClientFactory.verify(() -> ApiClientFactory.createTenantApiClient("tenantId", "basePath"),
                    times(1));
            }
        }
    }

}
