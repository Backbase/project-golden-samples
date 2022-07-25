package org.quarkus.openapi.generator.config.api.factory;

import io.opentracing.Tracer;
import io.opentracing.contrib.concurrent.TracedExecutorService;
import io.opentracing.util.GlobalTracer;
import io.smallrye.opentracing.SmallRyeClientTracingFeature;
import java.util.Objects;
import java.util.concurrent.Executors;
import javax.ws.rs.client.ClientBuilder;
import org.jboss.logging.Logger;
import org.quarkus.openapi.todo.api.ApiClient;
import org.slf4j.LoggerFactory;

public class ApiClientFactory {

    private ApiClientFactory() {
    }

    /**
     * Getting ApiClient configured for given tenant.
     *
     * @param basePath url path to given resource
     * @return {@link ApiClient}
     */
    public static ApiClient createTraceApiClient(String basePath) {
        ApiClient apiClient = new ApiClient().setBasePath(basePath);
        Tracer tracer = GlobalTracer.get();
        ClientBuilder clientBuilder = ClientBuilder.newBuilder()
            .executorService(new TracedExecutorService(Executors.newCachedThreadPool(), tracer))
            .register(new SmallRyeClientTracingFeature(tracer))
            .register(apiClient.getJSON());

        if (LoggerFactory.getLogger(ApiClient.class).isDebugEnabled()) {
            clientBuilder.register(Logger.class);
        }

        return apiClient.setHttpClient(clientBuilder.build());
    }

    /**
     * Getting ApiClient configured for given tenant (or without tenant if tenantId is null).
     *
     * @param tenantId id of given tenant
     * @param basePath url path to given resource
     * @return {@link ApiClient}
     */
    public static ApiClient createTenantApiClient(String tenantId, String basePath) {
        ApiClient apiClient = createTraceApiClient(basePath);
        if (Objects.isNull(tenantId)) {
            return apiClient;
        }

        return apiClient.addDefaultHeader("X-TID", tenantId);
    }
}
