package com.backbase.openbanking.mockserver.common;

import com.backbase.openbanking.mockserver.common.exceptions.MockDataException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * Encapsulates methods for JSON content deserialization and object conversion.
 */
@Component
public class SimpleObjectMapper {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleObjectMapper.class);
    private final ObjectMapper mapper;

    public SimpleObjectMapper() {
        mapper = new ObjectMapper();
    }

    public <T> T read(InputStream inputStream, Class<T> clazz) {
        try {
            return mapper.readValue(inputStream, clazz);
        } catch (IOException ex) {
            throw new MockDataException("Deserialization error for class " + clazz, ex);
        }
    }

    public <T> T read(String s, Class<T> clazz) {
        try {
            return mapper.readValue(s, clazz);
        } catch (IOException ex) {
            throw new MockDataException("Deserialization error for class " + clazz, ex);
        }
    }

    public <T> T convert(Object object, Class<T> clazz) {
        return mapper.convertValue(object, clazz);
    }

    public JsonNode convert(Object object) {
        return mapper.valueToTree(object);
    }

}
