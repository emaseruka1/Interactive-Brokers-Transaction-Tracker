package com.example.portfolio.service.filter;

import com.example.portfolio.service.calculation.AvgCostPerShareAggregation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AssetSymbolFilterTest {

    @Mock
    private DateFilter dateFilter;

    private AssetSymbolFilter assetSymbolFilter;

    @BeforeEach
    void setup(){assetSymbolFilter = new AssetSymbolFilter(dateFilter);
    }

    @Test
    void testGetStockOrders() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        JsonNode order1 = mapper.readTree("""
    {"tradeDate": "20250925", "assetCategory": "STK"}
""");
        JsonNode order2 = mapper.readTree("""
    {"tradeDate": "20250925", "assetCategory": "CASH"}
""");
        JsonNode order3 = mapper.readTree("""
    {"tradeDate": "20250925", "assetCategory": "CASH"}
""");
        JsonNode order4 = mapper.readTree("""
    {"tradeDate": "20251006", "assetCategory": "STK"}
""");
        JsonNode order5 = mapper.readTree("""
    {"tradeDate": "20251006", "assetCategory": "CASH"}
""");
        JsonNode order6 = mapper.readTree("""
    {"tradeDate": "20251006", "assetCategory": "CASH"}
""");
        JsonNode order7 = mapper.readTree("""
    {"tradeDate": "20251006", "assetCategory": "CASH"}
""");
        JsonNode order8 = mapper.readTree("""
    {"tradeDate": "20251006", "assetCategory": "CASH"}
""");
        JsonNode order9 = mapper.readTree("""
    {"tradeDate": "20251006", "assetCategory": "CASH"}
""");
        JsonNode order10 = mapper.readTree("""
    {"tradeDate": "20251006", "assetCategory": "CASH"}
""");
        JsonNode order11 = mapper.readTree("""
    {"tradeDate": "20251006", "assetCategory": "CASH"}
""");

        List<JsonNode> ordersFilteredByDate = List.of(order1, order2, order3, order4, order5, order6, order7, order8, order9, order10, order11);

        when(dateFilter.filterAllOrdersByDate()).thenReturn(ordersFilteredByDate);

        List<JsonNode> stockOrders = assetSymbolFilter.getStockOrders();

        assertEquals(2,stockOrders.size());

        assertEquals(ordersFilteredByDate.get(0),stockOrders.get(0));

        assertEquals(ordersFilteredByDate.get(3),stockOrders.get(1));
    }

    @Test
    void testGetStockTrades() throws JsonProcessingException{

        ObjectMapper mapper = new ObjectMapper();

        JsonNode trade1 = mapper.readTree("""
    {"tradeDate": "20250925", "assetCategory": "STK"}
""");
        JsonNode trade2 = mapper.readTree("""
    {"tradeDate": "20250925", "assetCategory": "CASH"}
""");

        List<JsonNode> tradesFilteredByDate = List.of(trade1,trade2);

        when(dateFilter.filterAllTradesByDate()).thenReturn(tradesFilteredByDate);

        List<JsonNode> stockTrades = assetSymbolFilter.getStockTrades();

        assertEquals(1,stockTrades.size());

        assertEquals(stockTrades.get(0),stockTrades.get(0));
    }
}
