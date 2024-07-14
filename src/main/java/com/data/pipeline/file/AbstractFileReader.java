package com.data.pipeline.file;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public abstract class AbstractFileReader<T> {

    public List<T> readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            try (InputStream fileInputStream = Files.newInputStream(path)) {
                return parseFile(fileInputStream, filePath);
            }
        } else {
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
