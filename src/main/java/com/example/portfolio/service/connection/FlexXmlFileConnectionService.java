package com.example.portfolio.service.connection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FlexXmlFileConnectionService {

    @Value("${flex.query.directory}")
    private String flexFileDirectory;

    @Value("${flex.query.filename}")
    private String filename;

    public File getFlexXmlFile() {

        Path filepath = Paths.get(flexFileDirectory, filename);
        return filepath.toFile();
    }

    public JsonNode parseFlexXmlFileToJson() {

        File flexXmlFile = getFlexXmlFile();

        XmlMapper xmlMapper = new XmlMapper();

        JsonNode FlexIbkrJsonData = null;

        try {
            FlexIbkrJsonData = xmlMapper.readTree(new File(String.valueOf(flexXmlFile)));
        }

        catch (IOException e) { throw new RuntimeException(e); }

        return FlexIbkrJsonData;
    }
}
