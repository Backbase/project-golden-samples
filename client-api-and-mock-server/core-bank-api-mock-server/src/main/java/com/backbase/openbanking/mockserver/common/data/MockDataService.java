package com.backbase.openbanking.mockserver.common.data;

import com.backbase.openbanking.mockserver.common.exceptions.MockDataException;
import org.springframework.stereotype.Component;

/**
 * A Generic service to read and process mocked data. This service provide a way to access a mock file, read it and create a {@link MockData}
 * from the mocked path. For the most use cases a mock server has to read a mock file and return it.
 * The idea is that a developer that needs to implement an endpoint that just need to return the mocked data from a file
 * just inject this service and not create another service to has the same boilerplate code.
 *
 * @author cesarl
 */
@Component
public class MockDataService {

    private final MockDataProperties mockDataProperties;
    private final MockPathProcessor processor;

    public MockDataService(MockDataProperties mockDataProperties, MockPathProcessor processor) {
        this.mockDataProperties = mockDataProperties;
        this.processor = processor;
    }

    /**
     * Reads the mock data file, with the specified parameters and return a {@link MockData} object with the representation
     * found in the file.
     * @param subdirectory The subdirectory where the mock files are locate, the subdirectory would be added to the {@link MockDataProperties#getRootPath()}
     * @param prefix The prefix for the mock data file
     * @param id the file id used to create with the prefix the mock data file
     * @return a {@link MockData} object representation
     * @throws MockDataException if an error occur reading the mock
     * file or if something fails processing the contents of the file.
     */
    public MockData readMockData(String subdirectory, String prefix, String id) {
        MockDataPath path = new MockDataPath.Builder(mockDataProperties)
                .withSubdirectory(subdirectory)
                .withPrefix(prefix)
                .build(id);
        return processor.process(path);
    }
}
