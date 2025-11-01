package com.example.portfolio.service.calculation;

import com.example.portfolio.service.filter.AssetSymbolFilter;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IbkrFxRateCalculation {

    private final AssetSymbolFilter assetSymbolFilter;

    @Autowired
    public IbkrFxRateCalculation(AssetSymbolFilter assetSymbolFilter){

        this.assetSymbolFilter = assetSymbolFilter;
    }

    public Map<String, Map<String, BigDecimal>> groupUsdPlnFxTradesByDate(){

        String symbol="USD.PLN";

        Map<String, Map<String, BigDecimal>> groupedUsdPlnFxTrades = new HashMap<>();

        List<JsonNode> cashTrades = assetSymbolFilter.getCashTrades();

        for (JsonNode trd : cashTrades) {

            if (trd.get("symbol").asText().equals(symbol)){

            String date = trd.get("tradeDate").asText();
            BigDecimal tradeMoney = new BigDecimal(trd.get("tradeMoney").asText()).abs();
            BigDecimal quantity = new BigDecimal(trd.get("quantity").asText());

            groupedUsdPlnFxTrades.computeIfAbsent(date, k -> new HashMap<>());

            groupedUsdPlnFxTrades.get(date).merge("sumOfTradeMoney", tradeMoney, BigDecimal::add);

            groupedUsdPlnFxTrades.get(date).merge("sumOfQuantity", quantity, BigDecimal::add);
            }
        }

        return groupedUsdPlnFxTrades;
    }

    public Map<String, BigDecimal> calculateUsdPlnFxRatePerDate() {

        Map<String, BigDecimal> UsdPlnFxRatePerDate = new HashMap<>();

        Map<String, Map<String, BigDecimal>> groupedUsdPlnFxTrades = groupUsdPlnFxTradesByDate();

        for (Map.Entry<String, Map<String, BigDecimal>> entry : groupedUsdPlnFxTrades.entrySet()) {

            String date = entry.getKey();
            Map<String, BigDecimal> tradeData = entry.getValue();

            BigDecimal sumNetCash = tradeData.getOrDefault("sumOfTradeMoney", BigDecimal.ZERO);
            BigDecimal sumQuantity = tradeData.getOrDefault("sumOfQuantity", BigDecimal.ONE);

            BigDecimal FxRate = sumNetCash.divide(sumQuantity, 10, RoundingMode.HALF_UP);

            UsdPlnFxRatePerDate.put(date, FxRate);
        }

        return UsdPlnFxRatePerDate;
    }
}
