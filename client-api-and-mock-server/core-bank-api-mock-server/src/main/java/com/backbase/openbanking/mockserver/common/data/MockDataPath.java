package com.backbase.openbanking.mockserver.common.data;

import com.backbase.openbanking.mockserver.common.exceptions.MockDataException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class represents the location of a mock file. It's an abstraction over the {@link Path} class, but the construction
 * of the path is enforced by the {@link Builder} class, where the configurations for the root path, file extension and
 * prefix separator are implements.
 *
 * @author cesarl
 */
public class MockDataPath {

    private Path filePath;

    private MockDataPath(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Read the contents of the mock file.
     * @return InputStream the represents the content of the file
     */
    public InputStream read() {
        try {
            return Files.newInputStream(filePath);
        } catch (IOException ex) {
            throw new MockDataException("Cannot read file " + filePath, ex);
        }
    }

    /**
     * Builder class for a {@link MockDataPath}, this class has utility method to construct a path for a mock file based
     * on the configurations set in the application.yml and provided with {@link MockDataProperties}
     *
     * @author cesarl
     */
    public static class Builder {

        private Path path;
        private MockDataProperties properties;
        private String prefix;

        /**
         * Creates a new builder to create a new {@link MockDataPath}
         * @param properties The configuration properties for the mock data files
         */
        public Builder(MockDataProperties properties) {
            this.properties = properties;
            this.path = properties.getRootPath();
        }

        /**
         * Configures subdirectory to the current path, all the paths starts with the {@link MockDataProperties#getRootPath()}
         * as the base root path directory
         * @param subdirectory the name of the subdirectory
         * @return this builder
         */
        public Builder withSubdirectory(String subdirectory) {
            this.path = this.path.resolve(subdirectory);
            return this;
        }

        /**
         * Define a prefix for a file name, the prefix is optional
         * @param prefix The prefix value
         * @return this builder
         */
        public Builder withPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        /**
         * Builds the {@link MockDataPath} with the given id as file name, the path is validated before to return it.
         * @param id the id to be used to create the file name. If a prefix was not configured the id is consider as full filename
         * @return the {@link MockDataPath} object
         */
        public MockDataPath build(String id) {
            String prefixName = StringUtils.isEmpty(prefix) ? "" : prefix + properties.getPrefixSeparator();
            String fileName = prefixName + id + properties.getFileExtension();
            Path filePath = this.path.resolve(fileName);
            validate(filePath);
            return new MockDataPath(filePath);
        }

        //Validates the mock file path exist and it's not a directory
        private void validate(Path filePath) {
            if (!Files.exists(filePath)) {
                throw new MockDataException("The file " + filePath + " doesn't exist");
            }
            if (Files.isDirectory(filePath)) {
                throw new MockDataException("The file" + filePath + " is a directory");
            }
        }

    }
}
