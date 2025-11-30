package com.example.portfolio.model;

import com.example.portfolio.service.connection.FlexXmlFileConnectionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class IbkrFlexJsonDataModalTest {

    private FlexXmlFileConnectionService flexXmlFileConnectionService;
    private IbkrFlexJsonDataModal ibkrFlexJsonDataModal;
    private JsonNode flexIbkrJsonData;

    @BeforeEach
    void setUp() throws Exception{

        flexXmlFileConnectionService = Mockito.mock(FlexXmlFileConnectionService.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode flexIbkrJsonData = mapper.readTree(new File("src/test/resources/output/json/parsedFlexXmlFileToJson.json"));
        when(flexXmlFileConnectionService.parseFlexXmlFileToJson()).thenReturn(flexIbkrJsonData);

        ibkrFlexJsonDataModal = new IbkrFlexJsonDataModal(flexXmlFileConnectionService);

    }

    @Test
    void testIterateThroughStatementJsonNodesForTradesWithData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        List<JsonNode> tradesWithDataList = ibkrFlexJsonDataModal.iterateThroughStatementJsonNodesForTradesWithData();
        JsonNode tradesWithData = mapper.valueToTree(tradesWithDataList);

        File expectedJson = new File("src/test/resources/output/json/tradesWithData.json");
        JsonNode expectedTradesWithDataJson = mapper.readTree(expectedJson);

        assertEquals(expectedTradesWithDataJson,tradesWithData);

    }

    @Test
    void testIterateThroughTradeJsonNodesForAllOrders() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        List<JsonNode> allOrdersList = ibkrFlexJsonDataModal.iterateThroughTradeJsonNodesForAllOrders();
        JsonNode allOrders = mapper.valueToTree(allOrdersList);

        File expectedJson = new File("src/test/resources/output/json/allOrders.json");
        JsonNode expectedAllOrdersJson = mapper.readTree(expectedJson);

        assertEquals(expectedAllOrdersJson, allOrders);
    }

    @Test
    void testIterateThroughTradeJsonNodesForAllTrades() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        List<JsonNode> allTradesList = ibkrFlexJsonDataModal.iterateThroughTradeJsonNodesForAllTrades();
        JsonNode allTrades = mapper.valueToTree(allTradesList);

        File expectedJson = new File("src/test/resources/output/json/allTrades.json");
        JsonNode expectedAllTradesJson = mapper.readTree(expectedJson);

        assertEquals(expectedAllTradesJson, allTrades);
    }
}
