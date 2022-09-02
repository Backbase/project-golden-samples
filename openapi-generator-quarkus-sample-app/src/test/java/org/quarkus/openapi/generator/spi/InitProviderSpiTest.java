package org.quarkus.openapi.generator.spi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.keycloak.provider.Provider;
import org.keycloak.provider.ProviderFactory;
import org.quarkus.openapi.generator.spi.factory.InitProviderFactory;
import org.quarkus.openapi.generator.spi.provider.InitProvider;

class InitProviderSpiTest {

    private final InitProviderSpi initProviderSpi = new InitProviderSpi();

    @Test
    void shouldNotBeInternal() {
        boolean resultInternalFlag = initProviderSpi.isInternal();
        assertFalse(resultInternalFlag);
    }

    @Test
    void shouldReturnName() {
        String resultName = initProviderSpi.getName();
        assertEquals("init-provider-spi", resultName);
    }

    @Test
    void shouldReturnProviderClass() {
        Class<? extends Provider> resultProviderClass = initProviderSpi.getProviderClass();
        assertEquals(InitProvider.class, resultProviderClass);
    }

    @Test
    void shouldReturnProviderFactoryClass() {
        Class<? extends ProviderFactory> resultProviderFactoryClass = initProviderSpi.getProviderFactoryClass();
        assertEquals(InitProviderFactory.class, resultProviderFactoryClass);
    }

}