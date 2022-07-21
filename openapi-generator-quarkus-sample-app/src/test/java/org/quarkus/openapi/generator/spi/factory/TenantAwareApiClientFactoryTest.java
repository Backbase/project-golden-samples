package org.quarkus.openapi.generator.spi.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.backbase.identity.m10y.models.Tenant;
import com.backbase.identity.m10y.providers.TenantResolverProvider;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.Config;
import org.keycloak.models.KeycloakContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quarkus.openapi.generator.TenantConfigurationUtils;
import org.quarkus.openapi.generator.config.TodoApiConfig;
import org.quarkus.openapi.generator.config.api.factory.TodoApiConfigFactory;
import org.quarkus.openapi.generator.spi.provider.ApiClientProvider;

@ExtendWith(MockitoExtension.class)
class TenantAwareApiClientFactoryTest {

    @Mock
    private org.eclipse.microprofile.config.Config mockConfig;
    @Mock
    private Config.Scope mockScope;
    @Mock
    private TodoApiConfig mockRbsApiConfig;
    @Mock
    private TodoApiConfig mockTenantRbsApiConfig;
    @Mock
    private KeycloakSession mockSession;
    @Mock
    private TenantResolverProvider mockTenantProvider;
    @Mock
    private Tenant mockTenant;
    @Mock
    private KeycloakContext mockContext;
    @Mock
    private RealmModel mockRealmModel;

    private TenantAwareApiClientFactory factory = new TenantAwareApiClientFactory();

    @Test
    void shouldUseDefaultWhenNoTenantClientsAvailable() {
        when(mockSession.getContext()).thenReturn(mockContext);
        when(mockContext.getRealm()).thenReturn(mockRealmModel);
        when(mockRealmModel.getId()).thenReturn("realmId");
        when(mockSession.getProvider(TenantResolverProvider.class)).thenReturn(mockTenantProvider);
        when(mockTenantProvider.resolveTenant("realmId")).thenReturn(Optional.of(mockTenant));
        when(mockTenant.getId()).thenReturn("tenantId");

        try (MockedStatic<TodoApiConfigFactory> mockRbsApiFactory = mockStatic(TodoApiConfigFactory.class)) {
            mockRbsApiFactory.when(TodoApiConfigFactory::createTodoApiConfig).thenReturn(mockRbsApiConfig);
            try (MockedStatic<TenantConfigurationUtils> mockTenantUtils = mockStatic(TenantConfigurationUtils.class)) {
                mockTenantUtils.when(TenantConfigurationUtils::getTenantForRealmIds).thenReturn(null);
                factory.init(Config.scope());

                ApiClientProvider resultApiClientProvider = factory.create(mockSession);

                assertEquals(mockRbsApiConfig, resultApiClientProvider.getApiConfig());
            }
        }
    }

    @Test
    void shouldUseDefaultWhenRealmIsNotTenant() {
        when(mockSession.getContext()).thenReturn(mockContext);
        when(mockContext.getRealm()).thenReturn(mockRealmModel);
        when(mockRealmModel.getId()).thenReturn("noneTenantRealmId");
        when(mockSession.getProvider(TenantResolverProvider.class)).thenReturn(mockTenantProvider);
        when(mockTenantProvider.resolveTenant("noneTenantRealmId")).thenReturn(Optional.empty());
        when(mockTenant.getId()).thenReturn("tenantId");

        try (MockedStatic<TodoApiConfigFactory> mockRbsApiFactory = mockStatic(TodoApiConfigFactory.class)) {
            mockRbsApiFactory.when(TodoApiConfigFactory::createTodoApiConfig).thenReturn(mockRbsApiConfig);
            mockRbsApiFactory.when(() -> TodoApiConfigFactory.createTodoApiConfig(mockTenant))
                .thenReturn(mockTenantRbsApiConfig);
            try (MockedStatic<TenantConfigurationUtils> mockTenantUtils = mockStatic(TenantConfigurationUtils.class)) {
                mockTenantUtils.when(TenantConfigurationUtils::getTenantForRealmIds)
                    .thenReturn(Map.of("realmId", mockTenant));
                factory.init(Config.scope());

                ApiClientProvider resultApiClientProvider = factory.create(mockSession);

                assertEquals(mockRbsApiConfig, resultApiClientProvider.getApiConfig());
            }
        }
    }

    @Test
    void shouldUseTenantApiWhenRealmIsTenant() {
        when(mockSession.getContext()).thenReturn(mockContext);
        when(mockContext.getRealm()).thenReturn(mockRealmModel);
        when(mockRealmModel.getId()).thenReturn("realmId");
        when(mockSession.getProvider(TenantResolverProvider.class)).thenReturn(mockTenantProvider);
        when(mockTenantProvider.resolveTenant("realmId")).thenReturn(Optional.of(mockTenant));
        when(mockTenant.getId()).thenReturn("tenantId");

        try (MockedStatic<TodoApiConfigFactory> mockRbsApiFactory = mockStatic(TodoApiConfigFactory.class)) {
            mockRbsApiFactory.when(TodoApiConfigFactory::createTodoApiConfig).thenReturn(mockRbsApiConfig);
            mockRbsApiFactory.when(() -> TodoApiConfigFactory.createTodoApiConfig(mockTenant))
                .thenReturn(mockTenantRbsApiConfig);
            try (MockedStatic<TenantConfigurationUtils> mockTenantUtils = mockStatic(TenantConfigurationUtils.class)) {
                mockTenantUtils.when(TenantConfigurationUtils::getTenantForRealmIds)
                    .thenReturn(Map.of("realmId", mockTenant));
                factory.init(Config.scope());

                ApiClientProvider resultApiClientProvider = factory.create(mockSession);

                assertEquals(mockTenantRbsApiConfig, resultApiClientProvider.getApiConfig());
            }
        }
    }

    @Test
    void shouldReturnId() {
        assertEquals("tenant-aware-api-client-provider", factory.getId());
    }

}
