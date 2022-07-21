package org.quarkus.openapi.generator.spi.factory;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import java.util.stream.Stream;
import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.quarkus.openapi.generator.common.RealmSessionCountFunction;
import org.quarkus.openapi.generator.spi.provider.InitProvider;

public class MicrometerCustomMetricsInit extends InitProvider implements InitProviderFactory {

    static final String PROVIDER_ID = "micrometer-metrics-provider";
    static final String CONFIG_SCOPE_CUSTOM_METRICS = "custom-metrics";
    static final String CONFIG_CUSTOM_METRICS_ENABLED = "enabled";
    static final String CONFIG_CUSTOM_METRICS_REALMS = "realms";
    static final String SESSION_COUNT_TITLE = "session.count";
    static final String REALM = "realm";

    @Override
    public InitProvider create(KeycloakSession keycloakSession) {
        return this;
    }

    @Override
    public void init(Config.Scope scope) {
        // No action needed
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
        CompositeMeterRegistry registry = Metrics.globalRegistry;
        Boolean enabled = Config.scope(CONFIG_SCOPE_CUSTOM_METRICS).getBoolean(CONFIG_CUSTOM_METRICS_ENABLED);

        if (Boolean.TRUE.equals(enabled)) {
            setupRealmSessionCountMetrics(registry);
        }
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    /**
     * Retrieves a list of realms configured by keycloak.custom-metrics.realms and configures session count metrics for
     * each given realm.
     *
     * @param registry CompositeMeterRegistry
     */
    private void setupRealmSessionCountMetrics(CompositeMeterRegistry registry) {
        String[] realmList = Config.scope(CONFIG_SCOPE_CUSTOM_METRICS).getArray(CONFIG_CUSTOM_METRICS_REALMS);

        Stream.of(realmList).forEach(name ->
            registry.gauge(SESSION_COUNT_TITLE, Tags.of(REALM, name), name, new RealmSessionCountFunction()));
    }
}
