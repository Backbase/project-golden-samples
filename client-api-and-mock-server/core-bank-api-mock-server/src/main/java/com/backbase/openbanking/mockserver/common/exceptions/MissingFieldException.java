package com.backbase.openbanking.mockserver.common.exceptions;

/**
 * Exception used to represent a missing field in a request of a controller.
 *
 * @author cesarl
 */
public class MissingFieldException extends RuntimeException {

    private String fieldName;

    /**
     * Create a new instance of the exception given the fieldName that is missing.
     * @param fieldName the fieldName missed that cause the exception to be thrown
     */
    public MissingFieldException(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
