package com.data.pipeline.file.txt;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class TextFileWriter {

    private static final String RESOURCE_BASE_PATH = "src/main/resources/";

    /**
     * Writes multiple lines of text to the specified file.
     * If the file exists, it will be overwritten.
     * The path can be an absolute file path or a relative resource path.
     *
     * @param filePath the path of the file to write
     * @param contents the list of strings to write to the file
     * @param isResourcePath true if the file path is a relative resource path, false if it's an absolute file path
     * @throws IOException if an I/O error occurs writing to the file
     */
    public void writeFile(String filePath, List<String> contents, boolean isResourcePath) throws IOException {
        Path path = resolvePath(filePath, isResourcePath);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (String line : contents) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Appends multiple lines of text to the specified file.
     * The path can be an absolute file path or a relative resource path.
     *
     * @param filePath the path of the file to append
     * @param contents the list of strings to append to the file
     * @param isResourcePath true if the file path is a relative resource path, false if it's an absolute file path
     * @throws IOException if an I/O error occurs writing to the file
     */
    public void appendToFile(String filePath, List<String> contents, boolean isResourcePath) throws IOException {
        Path path = resolvePath(filePath, isResourcePath);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            for (String line : contents) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private Path resolvePath(String filePath, boolean isResourcePath) throws IOException {
        Path path;
        if (isResourcePath) {
            path = Paths.get(RESOURCE_BASE_PATH, filePath);
        } else {
            path = Paths.get(filePath);
        }

        Path parentDir = path.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }
        return path;
    }
}
