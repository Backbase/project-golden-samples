package org.quarkus.openapi.generator.config.api.global;

import org.quarkus.openapi.generator.config.TodoApiConfig;

public class GlobalConfigConstants {

    private GlobalConfigConstants() {
    }

    public static final String BACKBASE_SCOPE = "backbase";
    /**
     * TodoApi Base URL / not added to configuration, just a illustration.
     *
     * @return {@link TodoApiConfig}
     */
    public static final String TODO_API_BASE_URL_KEY = "custom.todoApi.url";
}
