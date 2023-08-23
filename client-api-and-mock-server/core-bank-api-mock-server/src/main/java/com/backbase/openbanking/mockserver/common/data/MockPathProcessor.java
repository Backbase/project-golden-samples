package com.backbase.openbanking.mockserver.common.data;

import com.backbase.openbanking.mockserver.common.SimpleObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

/**
 * Process a mock data file {@link MockDataPath} and return it as a {@link MockData} object. It encapsulate the process
 * to construct the mock data object from a file, this definition of how to parse and construct the object could change
 * and the idea is separate and delegate to this object this concern. So if the parsing and construction change this object
 * would only to be changed.
 *
 * @author cesarl
 */
@Component
public class MockPathProcessor {

    private static final String STATUS_CODE_FIELD = "status-code";
    private static final String RESPONSE_FIELD = "response";
    private final SimpleObjectMapper mapper;

    public MockPathProcessor(SimpleObjectMapper mapper) {
        this.mapper = mapper;
    }

    public MockData process(MockDataPath path) {
        JsonNode node = mapper.read(path.read(), JsonNode.class);
        int status = node.get(STATUS_CODE_FIELD).asInt();
        JsonNode payload = node.get(RESPONSE_FIELD);
        return new MockData(status, payload, mapper);
    }
}
