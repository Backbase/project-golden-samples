package com.backbase.openbanking.mockserver.common.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for Spring web mvc
 * @author cesarl
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Register the common interceptor {@link MockControllerInterceptor} for spring mvc controller
     * @param registry The interceptor registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MockControllerInterceptor());
    }
}
