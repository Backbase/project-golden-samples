package org.quarkus.openapi.generator.spi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.keycloak.provider.Provider;
import org.keycloak.provider.ProviderFactory;
import org.quarkus.openapi.generator.spi.factory.ApiClientProviderFactory;
import org.quarkus.openapi.generator.spi.provider.ApiClientProvider;

class ApiClientProviderSpiTest {

    private ApiClientProviderSpi spi = new ApiClientProviderSpi();

    @Test
    void shouldNotBeInternalProvider() {
        boolean internal = spi.isInternal();
        assertFalse(internal);
    }

    @Test
    void shouldReturnValidName() {
        String name = spi.getName();
        assertEquals("api-client-provider-spi", name);
    }

    @Test
    void shouldReturnValidProviderClass() {
        Class<? extends Provider> providerClass = spi.getProviderClass();
        assertEquals(ApiClientProvider.class, providerClass);
    }

    @Test
    void shouldReturnValidFactoryClass() {
        Class<? extends ProviderFactory> providerFactoryClass = spi.getProviderFactoryClass();
        assertEquals(ApiClientProviderFactory.class, providerFactoryClass);
    }

}