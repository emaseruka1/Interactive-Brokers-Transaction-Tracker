package com.example.portfolio.service.calculation;

import com.example.portfolio.service.filter.AssetSymbolFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IbkrFxRateCalculationTest {

    @Mock
    private AssetSymbolFilter assetSymbolFilter;

    private IbkrFxRateCalculation ibkrFxRateCalculation;

    @BeforeEach
    void setup(){
        ibkrFxRateCalculation = new IbkrFxRateCalculation(assetSymbolFilter);
    }

    @Test
    void testGroupUsdPlnFxTradesByDate() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        JsonNode trade1 = mapper.readTree("""
  {"tradeDate":"20250925","symbol":"GBP.USD","tradeMoney":"-7883.1129619","quantity":"-5857.61"}
""");
        JsonNode trade2 = mapper.readTree("""
  {"tradeDate":"20250925","symbol":"GBP.USD","tradeMoney":"-0.538316","quantity":"-0.4"}
""");
        JsonNode trade3 = mapper.readTree("""
  {"tradeDate":"20250925","symbol":"GBP.USD","tradeMoney":"-113.5939134","quantity":"-84.41"}
""");
        JsonNode trade4 = mapper.readTree("""
  {"tradeDate":"20250925","symbol":"GBP.USD","tradeMoney":"-0.0037967","quantity":"-0.0028461"}
""");
        JsonNode trade5 = mapper.readTree("""
  {"tradeDate":"20251006","symbol":"USD.PLN","tradeMoney":"3682.7186","quantity":"1012"}
""");
        JsonNode trade6 = mapper.readTree("""
  {"tradeDate":"20251006","symbol":"USD.PLN","tradeMoney":"14.55568","quantity":"4"}
""");
        JsonNode trade7 = mapper.readTree("""
  {"tradeDate":"20251006","symbol":"USD.PLN","tradeMoney":"2.72572","quantity":"0.75037245"}
""");
        JsonNode trade8 = mapper.readTree("""
  {"tradeDate":"20251006","symbol":"GBP.USD","tradeMoney":"-19.334404","quantity":"-14.39"}
""");
        JsonNode trade9 = mapper.readTree("""
  {"tradeDate":"20251006","symbol":"GBP.USD","tradeMoney":"-226.4863317","quantity":"-168.33"}
""");
        JsonNode trade10 = mapper.readTree("""
  {"tradeDate":"20251006","symbol":"GBP.USD","tradeMoney":"-0.0134549","quantity":"-0.01"}
""");
        JsonNode trade11 = mapper.readTree("""
  {"tradeDate":"20251006","symbol":"GBP.USD","tradeMoney":"-0.0053574","quantity":"-0.00397405"}
""");

        List<JsonNode> cashTrades = List.of(trade1, trade2, trade3, trade4,trade5, trade6, trade7,trade8, trade9, trade10, trade11);

        when(assetSymbolFilter.getCashTrades()).thenReturn(cashTrades);

        Map<String, Map<String, BigDecimal>> groupedUsdPlnFxTrades = ibkrFxRateCalculation.groupUsdPlnFxTradesByDate();

        assertEquals(Map.of("sumOfQuantity", new BigDecimal("1016.75037245"), "sumOfTradeMoney", new BigDecimal("3700.00000")), groupedUsdPlnFxTrades.get("20251006"));
        assertNull(groupedUsdPlnFxTrades.get("20250125"));
    }

    @Test
    void testCalculateUsdPlnFxRatePerDate(){

        IbkrFxRateCalculation spy = spy(ibkrFxRateCalculation);

        Map<String, Map<String, BigDecimal>> groupedUsdPlnFxTrades = Map.of("20251006",Map.of("sumOfQuantity",new BigDecimal("1016.75037245"),"sumOfTradeMoney",new BigDecimal("3700.00000")));

        doReturn(groupedUsdPlnFxTrades).when(spy).groupUsdPlnFxTradesByDate();

        Map<String, BigDecimal> UsdPlnFxRatePerDate = spy.calculateUsdPlnFxRatePerDate();

        assertEquals(new BigDecimal("3.6390446468"), UsdPlnFxRatePerDate.get("20251006"));
        assertNull(UsdPlnFxRatePerDate.get("20250125"));
    }
}
