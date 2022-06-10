package com.backbase.openbanking.mockserver.common.web;

import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * This interceptor is generic for all the controllers in this project, and provides common functionality that is required
 * by all the controllers avoiding duplication of code and delegating that behaviour to this class.
 * A common concern is to log the execution of a controller method that is invoked when it's correspondent endpoint is invoked,
 * logging the request url, http method and headers, the logic here could varies depending of the project, but this class provides
 * a mechanism to implement that use cases without to duplicate the code in each method.
 *
 * @author cesarl
 */
@Log4j2
public class MockControllerInterceptor implements HandlerInterceptor {
    
    /**
     * Executed before actual handler is executed
     **/
    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        log.debug("Executing {} url: [{}]-[{}], {}", methodName(handler), request.getMethod(), request.getRequestURI(), request.getQueryString());
        logRequestHeaders(request);
        return true;
    }

    /**
     * Executed after complete request is finished
     **/
    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) throws Exception {
        if (ex != null) {
            ex.printStackTrace();
        }
        log.debug("[Executed] {}, [response] {}", methodName(handler), response);
        logResponseHeaders(response);
    }

    private String methodName(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            String controllerName = handlerMethod.getBeanType().getSimpleName();
            String methodName = handlerMethod.getMethod().getName();
            return String.format("Controller [%s] Method [%s]", controllerName, methodName);
        }
        return "";
    }

    private void logRequestHeaders(HttpServletRequest request) {
        log.debug("Request Headers: ");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            log.debug("\t Header [{}]: [{}]", headerName, request.getHeader(headerName));
        }
    }

    private void logResponseHeaders(HttpServletResponse response) {
        log.debug("Response Headers: ");
        for(String headerName: response.getHeaderNames()) {
            log.debug("\t Header [{}]: [{}]", headerName, response.getHeader(headerName));
        }
    }

}
