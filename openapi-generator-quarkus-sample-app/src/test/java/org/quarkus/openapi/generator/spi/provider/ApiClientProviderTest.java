package org.quarkus.openapi.generator.spi.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quarkus.openapi.generator.config.TodoApiConfig;

@ExtendWith(MockitoExtension.class)
class ApiClientProviderTest {

    @Mock
    private TodoApiConfig mockTodoApiConfig;

    @InjectMocks
    private ApiClientProvider provider;

    @Test
    void shouldGetValidConfig() {
        TodoApiConfig apiConfig = provider.getApiConfig();
        assertEquals(mockTodoApiConfig, apiConfig);
    }
}
