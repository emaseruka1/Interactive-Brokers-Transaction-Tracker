package com.example.portfolio.service.mapper;

import com.example.portfolio.model.GoogleSheetModal;
import com.example.portfolio.service.calculation.AvgCostPerShareAggregation;
import com.example.portfolio.service.calculation.IbkrFxRateCalculation;
import com.example.portfolio.service.connection.GoogleSheetConnectionService;
import com.example.portfolio.service.connection.NbpExchangeRateConnectionService;
import com.example.portfolio.service.filter.AssetSymbolFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FlexAndGoogleSheetModalsMapperTest {

    @Mock
    private AssetSymbolFilter assetSymbolFilter;

    @Mock
    private GoogleSheetConnectionService googleSheetConnectionService;

    @Mock
    private AvgCostPerShareAggregation avgCostPerShareAggregation;

    @Mock
    private IbkrFxRateCalculation ibkrFxRateCalculation;

    @Mock
    private NbpExchangeRateConnectionService nbpExchangeRateConnectionService;

    private FlexAndGoogleSheetModalsMapper flexAndGoogleSheetModalsMapper;
    private GoogleSheetModal googleSheetModal = new GoogleSheetModal();

    @BeforeEach
    void setup(){flexAndGoogleSheetModalsMapper = new FlexAndGoogleSheetModalsMapper(googleSheetModal,assetSymbolFilter,
            googleSheetConnectionService,
            avgCostPerShareAggregation,
            ibkrFxRateCalculation,
            nbpExchangeRateConnectionService);
    }


    @Test
    void createTransactionListForGoogleSheetsTest() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        JsonNode order1 = mapper.readTree("""
    {"tradeDate": "20251107", "assetCategory": "STK", "symbol":"VUAA", "tradeMoney":"999.993294", "tradePrice":"128.638009442", "quantity":"7.7737", "netCash":"-1003.993294", "ibCommission":"-4"}
""");
        List<JsonNode> stockOrders = List.of(order1);
        when(assetSymbolFilter.getStockOrders()).thenReturn(stockOrders);

        Map<String, BigDecimal> avgCostPerSharePerDate = Map.of("20251107", BigDecimal.valueOf(129.1525649));
        when(avgCostPerShareAggregation.calculateAvgCostPerSharePerDate()).thenReturn(avgCostPerSharePerDate);

        Map<String, BigDecimal> usdPlnFxRatePerDate = Map.of("20251107", BigDecimal.valueOf(3.675000455));
        when(ibkrFxRateCalculation.calculateUsdPlnFxRatePerDate()).thenReturn(usdPlnFxRatePerDate);

        when(nbpExchangeRateConnectionService.getNbpInterbankPlnUsdRate("20251107")).thenReturn(new BigDecimal("3.6818"));


        List<List<Object>> transactionListforGoogleSheets = flexAndGoogleSheetModalsMapper.createTransactionListForGoogleSheets();

        List<Object> expected = List.of("20251107","VUAA","buy",BigDecimal.valueOf(3700),new BigDecimal("999.993294"),new BigDecimal("128.638009442"),
                new BigDecimal("7.7737"),
                new BigDecimal("1003.993294"),
                new BigDecimal("129.1525649"),
                new BigDecimal("-0.184700"),
                new BigDecimal("4"),
                new BigDecimal("3.675000455"),
                new BigDecimal("3.6818")
        );
        assertEquals(expected,transactionListforGoogleSheets.get(0));
    }
}
