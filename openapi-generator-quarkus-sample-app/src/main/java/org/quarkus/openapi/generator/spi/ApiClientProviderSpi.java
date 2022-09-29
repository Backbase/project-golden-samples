package org.quarkus.openapi.generator.spi;

import org.keycloak.provider.Provider;
import org.keycloak.provider.ProviderFactory;
import org.keycloak.provider.Spi;
import org.quarkus.openapi.generator.spi.factory.ApiClientProviderFactory;
import org.quarkus.openapi.generator.spi.provider.ApiClientProvider;

public class ApiClientProviderSpi implements Spi {

    @Override
    public boolean isInternal() {
        return false;
    }

    @Override
    public String getName() {
        return "api-client-provider-spi";
    }

    @Override
    public Class<? extends Provider> getProviderClass() {
        return ApiClientProvider.class;
    }

    @Override
    public Class<? extends ProviderFactory> getProviderFactoryClass() {
        return ApiClientProviderFactory.class;
    }
}
