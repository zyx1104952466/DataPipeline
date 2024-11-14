package com.data.pipeline.file;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public abstract class AbstractFileReader<T> {

    public static final Logger LOGGER = LogManager.getLogger(AbstractFileReader.class);

    private String CHARSET = "GB2312";

    public String getCHARSET() {
        return CHARSET;
    }

    public void setCHARSET(String CHARSET) {
        this.CHARSET = CHARSET;
    }

    public List<T> readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            LOGGER.info("Reading file from path: {}", path);
            try (InputStream fileInputStream = Files.newInputStream(path)) {
                return parseFile(fileInputStream, filePath);
            }
        } else {
            LOGGER.info("Reading file from classpath: {}", filePath);
            try (InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
                if (resourceStream == null) {
                    throw new IOException("Resource not found: " + filePath);
                }
                return parseFile(resourceStream, filePath);
            }
        }
    }

    protected abstract List<T> parseFile(InputStream inputStream, String filePath) throws IOException;
}
