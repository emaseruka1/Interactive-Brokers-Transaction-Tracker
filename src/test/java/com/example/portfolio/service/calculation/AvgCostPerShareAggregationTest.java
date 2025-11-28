package com.example.portfolio.service.calculation;

import com.example.portfolio.service.filter.AssetSymbolFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AvgCostPerShareAggregationTest {

    @Mock
    private AssetSymbolFilter assetSymbolFilter;

    private AvgCostPerShareAggregation avgCostPerShareAggregation;

    @BeforeEach
    void setup(){
        avgCostPerShareAggregation = new AvgCostPerShareAggregation(assetSymbolFilter);
    }

    @Test
    void testGroupShareTradesByDate() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        JsonNode trade1 = mapper.readTree("""
            {"tradeDate":"20250925","quantity":"62","netCash":"-7890.4"}
        """);
        JsonNode trade2 = mapper.readTree("""
            {"tradeDate":"20250925","quantity":"0.8929","netCash":"-113.594738"}
        """);
        JsonNode trade3 = mapper.readTree("""
            {"tradeDate":"20251006","quantity":"0.7569","netCash":"-99.279548"}
        """);
        JsonNode trade4 = mapper.readTree("""
            {"tradeDate":"20251006","quantity":"8","netCash":"-1033.66"}
        """);
        JsonNode trade5 = mapper.readTree("""
            {"tradeDate":"20251006","quantity":"1","netCash":"-128.9"}
        """);

        List<JsonNode> stockTrades = List.of(trade1, trade2, trade3, trade4, trade5);

        when(assetSymbolFilter.getStockTrades()).thenReturn(stockTrades);

        Map<String, Map<String, BigDecimal>>  groupedShareTrades = avgCostPerShareAggregation.groupShareTradesByDate();

        assertEquals(Map.of("sumOfQuantity", new BigDecimal("9.7569"), "sumOfNetCash", new BigDecimal("1261.839548")), groupedShareTrades.get("20251006"));
        assertEquals(Map.of("sumOfQuantity", new BigDecimal("62.8929"), "sumOfNetCash", new BigDecimal("8003.994738")), groupedShareTrades.get("20250925"));
        assertNull(groupedShareTrades.get("20250125"));
    }

    @Test
    void testCalculateAvgCostPerSharePerDate(){

        AvgCostPerShareAggregation spyAggregation = Mockito.spy(avgCostPerShareAggregation);

        Map<String, Map<String, BigDecimal>> groupedShareTrades = Map.of(
                "20250925", Map.of("sumOfQuantity", new BigDecimal("62.8929"),"sumOfNetCash", new BigDecimal("8003.994738")
                ),
                "20251006", Map.of("sumOfQuantity", new BigDecimal("9.7569"),"sumOfNetCash", new BigDecimal("1261.839548")
                )
        );

        doReturn(groupedShareTrades).when(spyAggregation).groupShareTradesByDate();

        Map<String, BigDecimal> avgCostPerSharePerDate = spyAggregation.calculateAvgCostPerSharePerDate();

        assertEquals(new BigDecimal("127.2638841268"), avgCostPerSharePerDate.get("20250925"));
        assertEquals(new BigDecimal("129.3279164489"), avgCostPerSharePerDate.get("20251006"));
        assertNull(groupedShareTrades.get("20250125"));

    }
}
