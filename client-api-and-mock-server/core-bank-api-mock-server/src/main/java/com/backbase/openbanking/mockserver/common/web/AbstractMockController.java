package com.backbase.openbanking.mockserver.common.web;

import com.backbase.openbanking.mockserver.common.exceptions.MissingFieldException;
import com.backbase.openbanking.mockserver.common.data.MockData;
import com.backbase.openbanking.mockserver.common.data.MockDataService;
import com.backbase.openbanking.mockserver.common.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Abstract class where all the common logic used by the mock data controllers is defined. This class provides most of the logic
 * that a mock serve should implement, the idea behind this class is to allow the developers to extend this class and with that
 * each method of a controller just have to call one or two methods of this class to return the mock data.
 *
 * @author cesarl
 */
public abstract class AbstractMockController {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMockController.class);
    @Autowired
    public MockDataService service;

    /**
     * Create a {@link ResponseEntity} for a given mock file id, the path is configured in the method defineMockDataPathConfiguration.
     * If an error occur while processing the mock data the abstract method handleError is used to manage the exception and return the
     * corresponding error response.
     *
     * @param id the id of the mock file data
     * @return a {@link ResponseEntity} with the mocked data as response
     */
    public ResponseEntity mockDataResponse(String id) {
        return this.mockDataResponse(id, this::handleError);
    }

    /**
     * Create a {@link ResponseEntity} for a given mock file id, the path is configured in the method defineMockDataPathConfiguration
     * this method allows to configure a function the is used to precess an exception when an error occur processing the mock data.
     * <pre>{@code
     * return mockDataResponse(accountId, ex -> {
     *     String message = "Processed " + ex.getMessage();
     *     return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
     * });
     * }</pre>
     *
     * @param id             the id of the mock file data
     * @param errorProcessor the function that process the exception
     * @return a {@link ResponseEntity} with the mocked data as response
     */
    public ResponseEntity mockDataResponse(String id, Function<Exception, ResponseEntity> errorProcessor) {
        return mockDataResponse(id, null, errorProcessor);
    }

    /**
     * Create a {@link ResponseEntity} for a given mock file id, the path is configured in the method defineMockDataPathConfiguration.
     * If an error occur while processing the mock data the abstract method handleError is used to manage the exception and return the
     * corresponding error response.
     * This method allows to process the content of the mocked data, sometimes the mocked data should be processed and modified
     * before returning it, the param dataProcessor allows to do that.
     * <pre>{@code
     * return mockDataResponse(accountId, mockData -> {
     *      if (mockData.getHttpStatus().is2xxSuccessful()) {
     *            mockData.processPayload(TransactionsRetrieved.class, tx -> this.paginate(tx, offset, limit));
     *      }
     * });
     * }</pre>
     *
     * @param id            the id of the mock file data
     * @param dataProcessor The function that process the mock data
     * @return a {@link ResponseEntity} with the mocked data as response
     */
    public ResponseEntity mockDataResponse(String id, Consumer<MockData> dataProcessor) {
        return mockDataResponse(id, dataProcessor, this::handleError);
    }

    /**
     * Create a {@link ResponseEntity} for a given mock file id, the path is configured in the method defineMockDataPathConfiguration.
     * If an error occur while processing the mock data the function defined in the errorProcessor parameter is used to manage the exception and return the
     * corresponding error response.
     * This method allows to process the content of the mocked data, sometimes the mocked data should be processed and modified
     * before returning it, the param dataProcessor allows to do that.
     * <pre>{@code
     * return mockDataResponse(accountId, mockData -> {
     *      if (mockData.getHttpStatus().is2xxSuccessful()) {
     *            mockData.processPayload(TransactionsRetrieved.class, tx -> this.paginate(tx, offset, limit));
     *      }
     * }, ex -> {
     *        String message = "Processed " + ex.getMessage();
     *         return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
     * });
     * }</pre>
     * <p>
     * or
     * <pre>{@code
     * return mockDataResponse(accountId, this::processMockData, this::manageConversionError);
     * }</pre>
     *
     * @param id             the id of the mock file data
     * @param dataProcessor  The function that process the mock data
     * @param errorProcessor the function that process the exception
     * @return a {@link ResponseEntity} with the mocked data as response
     */
    public ResponseEntity mockDataResponse(String id, Consumer<MockData> dataProcessor, Function<Exception, ResponseEntity> errorProcessor) {
        MockDataPathConfiguration configuration = defineMockDataPathConfiguration();
        Objects.requireNonNull(configuration, "MockDataPathConfiguration cannot be null");
        try {
            MockData mockData = service.readMockData(configuration.getDirectory(), configuration.getPrefix(), id);
            if (dataProcessor != null) {
                dataProcessor.accept(mockData);
            }
            return mockData.createResponse();
        } catch (Exception ex) {
            return errorProcessor.apply(ex);
        }
    }

    /**
     * Validates a field that is not null and is not an empty String
     *
     * @param field     the field value to be validated
     * @param fieldName The field name to be used in a message error to indicate the missing value
     */
    protected void validateField(String field, String fieldName) {
        validateField(field, StringUtil::isAnyEmpty, fieldName);
    }

    /**
     * Validates a field that is not null and is not an empty String, the field value is computed by a {@link Supplier}
     * function and is returned if the validation pass
     *
     * <pre>{@code
     *     int timeout = validateField(() -> request.getParameter("Content-Type"), "Header: Content-Type");
     * }</pre>
     *
     * @param supplier  the function used to compute the field
     * @param fieldName The field name to be used in a message error to indicate the missing value
     */
    protected String validateField(Supplier<String> supplier, String fieldName) {
        validateField(supplier.get(), fieldName);
        return supplier.get();
    }

    /**
     * Validates a field using a specify a validator function, if the value passes the validation it's returned.
     *
     * <pre>{@code
     *  //if the value is equals to 0 an MissingFieldException will be raised
     *  int timeout = validateField(() -> request.getParameter("timeout"), v -> v == 0, "QueryString: timeout");
     * }</pre>
     *
     * @param supplier  the function that produces the value
     * @param validator the validator function
     * @param fieldName The field name to be used in a message error to indicate the missing value
     */
    protected <T> T validateField(Supplier<T> supplier, Predicate<T> validator, String fieldName) {
        T field = supplier.get();
        validateField(field, validator, fieldName);
        return field;
    }

    /**
     * Validates a field that is not null and is not an empty String, this method allows to specify a validator function
     * that indicates if the value is not valid
     *
     * <pre>{@code
     *  int timeout = request.getParameter("timeout");
     *  //if the value is equals to 0 an MissingFieldException will be raised
     *  validateField(4000, v -> v == 0, "QueryString: timeout");
     * }</pre>
     *
     * @param field     the field value to be validated
     * @param validator the validator function
     * @param fieldName The field name to be used in a message error to indicate the missing value
     */
    protected <T> void validateField(T field, Predicate<T> validator, String fieldName) {
        if (validator.test(field)) {
            logger.error("The field " + fieldName + " required by the Mock Server wasn't provided by the requester.");
            throw new MissingFieldException(fieldName);
        }
    }

    /**
     * Defined the subdirectory and prefix for the mocked files used in this controller
     *
     * @return MockDataPathConfiguration with the subdirectory and prefix values
     */
    protected abstract MockDataPathConfiguration defineMockDataPathConfiguration();

    /**
     * Common Error handler method that is called when a error occur processing the mocked data.
     *
     * @param ex The exception thrown
     * @return the ResponseEntity for the given exception
     */
    protected abstract ResponseEntity handleError(Exception ex);

    /**
     * Common configuration class that all mock controllers should provide to access a mock file: the mock directory
     * and the prefix of the mock files
     */
    public static class MockDataPathConfiguration {

        private String directory;
        private String prefix;

        public MockDataPathConfiguration(String directory, String prefix) {
            this.directory = directory;
            this.prefix = prefix;
        }

        public String getDirectory() {
            return directory;
        }

        public String getPrefix() {
            return prefix;
        }
    }
}
