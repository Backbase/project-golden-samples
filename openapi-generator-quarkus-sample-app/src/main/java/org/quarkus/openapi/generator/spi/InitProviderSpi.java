package org.quarkus.openapi.generator.spi;

import org.keycloak.provider.Provider;
import org.keycloak.provider.ProviderFactory;
import org.keycloak.provider.Spi;
import org.quarkus.openapi.generator.spi.factory.InitProviderFactory;
import org.quarkus.openapi.generator.spi.provider.InitProvider;

public class InitProviderSpi implements Spi {

    @Override
    public boolean isInternal() {
        return false;
    }

    @Override
    public String getName() {
        return "init-provider-spi";
    }

    @Override
    public Class<? extends Provider> getProviderClass() {
        return InitProvider.class;
    }

    @Override
    public Class<? extends ProviderFactory> getProviderFactoryClass() {
        return InitProviderFactory.class;
    }
}
