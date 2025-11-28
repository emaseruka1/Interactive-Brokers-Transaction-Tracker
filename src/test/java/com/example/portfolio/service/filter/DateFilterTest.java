package com.example.portfolio.service.filter;

import com.example.portfolio.model.IbkrFlexJsonDataModal;
import com.example.portfolio.service.connection.GoogleSheetConnectionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DateFilterTest {

    @Mock
    private IbkrFlexJsonDataModal ibkrFlexJsonDataModal;

    @Mock
    private GoogleSheetConnectionService googleSheetConnectionService;

    private DateFilter dateFilter;

    @BeforeEach
    void setup() throws IOException {

        List<LocalDate> mockDates = List.of(LocalDate.of(2025, 9, 25));
        when(googleSheetConnectionService.getDatesOfPastTransactionsOnGoogleSheets()).thenReturn(mockDates);

        dateFilter = new DateFilter(ibkrFlexJsonDataModal, googleSheetConnectionService);}

    @Test
    void filterAllOrdersByDate() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        JsonNode tradeBefore = mapper.readTree("""
            {"tradeDate": "20250920"}
        """);
        JsonNode tradeAfter = mapper.readTree("""
            {"tradeDate": "20251001"}
        """);

        JsonNode tradesArray = mapper.createArrayNode().add(tradeBefore).add(tradeAfter);
        List<JsonNode> tradesWithData = List.of(tradesArray);
        when(ibkrFlexJsonDataModal.getAllOrders()).thenReturn(tradesWithData);


        List<JsonNode> ordersFilteredByDate = dateFilter.filterAllOrdersByDate();

        assertEquals(1, ordersFilteredByDate.size());
        assertEquals("20251001", ordersFilteredByDate.get(0).get("tradeDate").asText());

    }
}
