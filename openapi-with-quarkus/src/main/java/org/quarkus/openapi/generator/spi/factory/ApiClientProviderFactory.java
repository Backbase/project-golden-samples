package org.quarkus.openapi.generator.spi.factory;

import org.keycloak.provider.ProviderFactory;
import org.quarkus.openapi.generator.spi.provider.ApiClientProvider;

public interface ApiClientProviderFactory extends ProviderFactory<ApiClientProvider> {
}
