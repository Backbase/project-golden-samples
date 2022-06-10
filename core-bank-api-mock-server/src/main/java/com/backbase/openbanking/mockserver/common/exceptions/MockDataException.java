package com.backbase.openbanking.mockserver.common.exceptions;

/**
 * Runtime exception thrown when any error occur with the classes that process the mock data files.
 * @author cesarl
 */
public class MockDataException extends RuntimeException {

    public MockDataException(String message) {
        super(message);
    }

    public MockDataException(String message, Throwable cause) {
        super(message, cause);
    }
}