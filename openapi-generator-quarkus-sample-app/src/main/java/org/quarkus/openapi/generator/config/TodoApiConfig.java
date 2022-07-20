package org.quarkus.openapi.generator.config;

import org.quarkus.openapi.todo.api.TodosApi;

public class TodoApiConfig {

    private final TodosApi todosApi;

    /**
     * Todos Api config.
     */
    public TodoApiConfig(TodosApi todosApi) {
        this.todosApi = todosApi;
    }

    public TodosApi getTodosApi() {
        return todosApi;
    }
}
