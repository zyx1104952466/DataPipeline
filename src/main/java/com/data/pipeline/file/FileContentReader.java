package com.data.pipeline.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileContentReader {

    /**
     * Reads the content of a file from the specified path.
     * The path can be a resource path within the project's resources directory or an absolute file path.
     *
     * @param filePath the path of the file to read
     * @return a List of strings, each representing a line of the file
     * @throws IOException if an I/O error occurs reading from the file
     */
    public static List<String> readFileContent(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            // If the path is an absolute file path
            try (Stream<String> lines = Files.lines(path)) {
                return lines.collect(Collectors.toList());
            }
        } else {
            // If the path is a resource path
            try (InputStream resourceStream = FileContentReader.class.getClassLoader().getResourceAsStream(filePath)) {
                if (resourceStream == null) {
                    throw new IOException("Resource not found: " + filePath);
                }
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceStream))) {
                    return reader.lines().collect(Collectors.toList());
                }
            }
        }
    }
}
