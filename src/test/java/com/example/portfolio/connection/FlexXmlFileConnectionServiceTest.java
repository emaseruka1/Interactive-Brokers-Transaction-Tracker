package com.example.portfolio.connection;

import com.example.portfolio.service.connection.FlexXmlFileConnectionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = FlexXmlFileConnectionService.class)
@TestPropertySource(properties = {
        "flex.query.directory=src/test/resources/input",
        "flex.query.filename=transactions_20251107.xml"
})
public class FlexXmlFileConnectionServiceTest {

    @Autowired
    private FlexXmlFileConnectionService flexXmlFileConnectionService;

    @Test
    void testParseFlexXmlFileToJson() throws IOException {

        JsonNode FlexIbkrJsonData = flexXmlFileConnectionService.parseFlexXmlFileToJson();

        ObjectMapper mapper = new ObjectMapper();
        File expectedFile = new File("src/test/resources/output/json/parsedFlexXmlFileToJson.json");
        JsonNode expectedJsonParsedFromXmlFile = mapper.readTree(expectedFile);

        assertEquals(expectedJsonParsedFromXmlFile,FlexIbkrJsonData);
    }

}
