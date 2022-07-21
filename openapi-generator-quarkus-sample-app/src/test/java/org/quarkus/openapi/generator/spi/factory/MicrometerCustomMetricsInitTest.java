package org.quarkus.openapi.generator.spi.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.quarkus.openapi.generator.spi.factory.MicrometerCustomMetricsInit.CONFIG_CUSTOM_METRICS_ENABLED;
import static org.quarkus.openapi.generator.spi.factory.MicrometerCustomMetricsInit.CONFIG_SCOPE_CUSTOM_METRICS;
import static org.quarkus.openapi.generator.spi.factory.MicrometerCustomMetricsInit.REALM;
import static org.quarkus.openapi.generator.spi.factory.MicrometerCustomMetricsInit.SESSION_COUNT_TITLE;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.Config;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quarkus.openapi.generator.spi.provider.InitProvider;

@ExtendWith(MockitoExtension.class)
class MicrometerCustomMetricsInitTest {

    @Mock
    private CompositeMeterRegistry registry;

    private final MicrometerCustomMetricsInit micrometerCustomMetricsInit = new MicrometerCustomMetricsInit();

    @Test
    void shouldCreateProvider() {
        InitProvider resultProvider = micrometerCustomMetricsInit.create(null);
        assertEquals(micrometerCustomMetricsInit, resultProvider);
    }

    @Test
    void shouldReturnId() {
        String resultId = micrometerCustomMetricsInit.getId();
        assertEquals(MicrometerCustomMetricsInit.PROVIDER_ID, resultId);
    }

    @Test
    void shouldInitMetricsWhenEnabled() throws Exception {
        setFinalStatic(Metrics.class.getDeclaredField("globalRegistry"), registry);
        Config.Scope scope = Mockito.mock(Config.Scope.class);
        Mockito.when(scope.getBoolean(CONFIG_CUSTOM_METRICS_ENABLED)).thenReturn(true);
        Mockito.when(scope.getArray(MicrometerCustomMetricsInit.CONFIG_CUSTOM_METRICS_REALMS)).thenReturn(
            new String[] {"coutts", "natwestplus", "ccd", "nwa"});

        wrapWithMock(() -> micrometerCustomMetricsInit.postInit(null), scope);

        verify(registry).gauge(eq(SESSION_COUNT_TITLE), eq(Tags.of(REALM, "coutts")), eq("coutts"), any());
        verify(registry).gauge(eq(SESSION_COUNT_TITLE), eq(Tags.of(REALM, "natwestplus")), eq("natwestplus"), any());
        verify(registry).gauge(eq(SESSION_COUNT_TITLE), eq(Tags.of(REALM, "ccd")), eq("ccd"), any());
        verify(registry).gauge(eq(SESSION_COUNT_TITLE), eq(Tags.of(REALM, "nwa")), eq("nwa"), any());
    }

    @Test
    void shouldNotInitMetricsWhenDisabled() throws Exception {
        setFinalStatic(Metrics.class.getDeclaredField("globalRegistry"), registry);
        Config.Scope scope = Mockito.mock(Config.Scope.class);
        Mockito.when(scope.getBoolean(CONFIG_CUSTOM_METRICS_ENABLED)).thenReturn(false);
        Mockito.lenient().when(scope.getArray(MicrometerCustomMetricsInit.CONFIG_CUSTOM_METRICS_REALMS)).thenReturn(
            new String[] {"coutts", "natwestplus", "ccd", "nwa"});

        wrapWithMock(() -> micrometerCustomMetricsInit.postInit(null), scope);

        verify(registry, times(0)).gauge(anyString(), any(Iterable.class), anyString(), any());
    }

    private void wrapWithMock(Runnable testedMethod, Config.Scope scope) {
        try (MockedStatic<Config> config = Mockito.mockStatic(Config.class)) {
            config.when(() -> Config.scope(CONFIG_SCOPE_CUSTOM_METRICS)).thenReturn(scope);
            testedMethod.run();
        }
    }

    static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }
}