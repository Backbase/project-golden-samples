package com.backbase.openbanking.mockserver.common.data;

import com.backbase.openbanking.mockserver.common.SimpleObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Function;

/**
 * Data structure representation for the the mocked json data. It allows to parse the content of the json and extract
 * the status code and payload. This representation could be converted to a Response Entity, the idea behind this is in
 * most cases the mocked data must not be deserialized and just to be return as is. If it has to me modified it has to be
 * serialized and processed this class also provide a method for that.
 *
 * @author cesarl
 */
public class MockData {

    private HttpStatus status;
    private JsonNode payload;
    private SimpleObjectMapper mapper;

    /**
     * Create a mocked data from the status code and json object payload
     *
     * @param status  the http status code value
     * @param payload the json object representation of the payload
     */
    protected MockData(int status, JsonNode payload, SimpleObjectMapper objectMapper) {
        this.status = HttpStatus.valueOf(status);
        this.payload = payload;
        this.mapper = objectMapper;
    }

    /**
     * Get the {@link HttpStatus} representation of the mocked data
     *
     * @return the http status
     */
    public HttpStatus getHttpStatus() {
        return status;
    }

    /**
     * Deserialize the mocked data payload to a given object
     *
     * @param clazz The class to be deserialized
     * @param <T>   the type to be deserialized
     * @return the object instance deserialized from the json node
     */
    public <T> T getPayload(Class<T> clazz) {
        return mapper.convert(payload, clazz);
    }

    /**
     * Manipulate the mocked data payload, this method is mutable it modifies the mocked data.
     * <pre>{@code
     * mockData.processPayload(TransactionsRetrieved.class, tx -> this.paginate(tx, offset, limit));
     * }</pre>
     * @param clazz    The class to be deserialized
     * @param function map function that alter the payload
     * @param <T>      the type to be deserialized
     * @param <R>      the result type for the manipulated payload it could be the same as T
     */
    public <T, R> void processPayload(Class<T> clazz, Function<T, R> function) {
        T payload = getPayload(clazz);
        R result = function.apply(payload);
        this.payload = mapper.convert(result);
    }

    /**
     * Create a {@link ResponseEntity} based on the mocked data, there is no need to deserialize or to provide a
     * class to generate a ResponseEntity it could directly return the json node data.
     *
     * @return a ResponseEntity with the mocked data
     */
    public ResponseEntity createResponse() {
        return new ResponseEntity<>(payload, status);
    }
}
