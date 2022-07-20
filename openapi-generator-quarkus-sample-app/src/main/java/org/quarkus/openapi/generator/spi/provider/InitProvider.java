package org.quarkus.openapi.generator.spi.provider;

import org.keycloak.provider.Provider;

public class InitProvider implements Provider {

    @Override
    public void close() {
        // No action needed
    }
}
