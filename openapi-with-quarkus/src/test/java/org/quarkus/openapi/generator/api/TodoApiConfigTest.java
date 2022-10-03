package org.quarkus.openapi.generator.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quarkus.openapi.generator.config.TodoApiConfig;
import org.quarkus.openapi.todo.api.TodosApi;

@ExtendWith(MockitoExtension.class)
class TodoApiConfigTest {

    @Mock
    private TodosApi todosApi;

    @InjectMocks
    private TodoApiConfig todoApiConfig;

    @Test
    void shouldReturnAllApis() {
        assertNotNull(todoApiConfig.getTodosApi());
    }
}
