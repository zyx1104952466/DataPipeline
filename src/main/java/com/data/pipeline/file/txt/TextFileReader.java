package com.data.pipeline.file.txt;

import com.data.pipeline.file.AbstractFileReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class TextFileReader extends AbstractFileReader<String> {

    @Override
    protected List<String> parseFile(InputStream inputStream, String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, getCHARSET()))) {
            return reader.lines().collect(Collectors.toList());
        }
    }
}
