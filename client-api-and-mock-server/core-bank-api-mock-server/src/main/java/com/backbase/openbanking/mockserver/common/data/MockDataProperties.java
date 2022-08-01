package com.backbase.openbanking.mockserver.common.data;

import com.backbase.openbanking.mockserver.common.exceptions.MockDataException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.nio.file.Path;

/**
 * Contains the configuration properties for the mock server files, those are defined with the cookbook prefix in the
 * application.yml file
 *
 * @author cesarl
 */
@Configuration
@ConfigurationProperties(prefix = "mock-server")
public class MockDataProperties {

    private Resource rootPath;
    private String fileExtension;
    private String prefixSeparator;

    public Path getRootPath() {
        try {
            return rootPath.getFile().toPath();
        } catch (Exception e) {
            throw new MockDataException("Cannot access the mock data path " + rootPath);
        }
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getPrefixSeparator() {
        return prefixSeparator;
    }

    public void setRootPath(Resource rootPath) {
        this.rootPath = rootPath;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public void setPrefixSeparator(String prefixSeparator) {
        this.prefixSeparator = prefixSeparator;
    }
}
