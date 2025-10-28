package com.example.portfolio.service.calculation;

import com.example.portfolio.service.filter.AssetSymbolFilter;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AvgCostPerShareAggregation {

    private final AssetSymbolFilter assetSymbolFilter;

    @Autowired
    public AvgCostPerShareAggregation(AssetSymbolFilter assetSymbolFilter){

        this.assetSymbolFilter = assetSymbolFilter;
    }

    public Map<String, Map<String, BigDecimal>> groupShareTradesByDate(){

        Map<String, Map<String, BigDecimal>> groupedShareTrades = new HashMap<>();

        List<JsonNode> stockTrades = assetSymbolFilter.getStockTrades();

        for (JsonNode trd : stockTrades) {

                String date = trd.get("tradeDate").asText();
                BigDecimal netCash = new BigDecimal(trd.get("netCash").asText()).abs();
                BigDecimal quantity = new BigDecimal(trd.get("quantity").asText());

                groupedShareTrades.computeIfAbsent(date, k -> new HashMap<>());

                groupedShareTrades.get(date).merge("sumOfNetCash", netCash, BigDecimal::add);

                groupedShareTrades.get(date).merge("sumOfQuantity", quantity, BigDecimal::add);
            }

        return groupedShareTrades;
    }

    public Map<String, BigDecimal> calculateAvgCostPerSharePerDate() {

        Map<String, BigDecimal> avgCostPerSharePerDate = new HashMap<>();

        Map<String, Map<String, BigDecimal>> groupedShareTrades = groupShareTradesByDate();

        for (Map.Entry<String, Map<String, BigDecimal>> entry : groupedShareTrades.entrySet()) {

            String date = entry.getKey();
            Map<String, BigDecimal> tradeData = entry.getValue();

            BigDecimal sumNetCash = tradeData.getOrDefault("sumOfNetCash", BigDecimal.ZERO);
            BigDecimal sumQuantity = tradeData.getOrDefault("sumOfQuantity", BigDecimal.ONE);

            BigDecimal avgCost = sumNetCash.divide(sumQuantity, 10, RoundingMode.HALF_UP);

            avgCostPerSharePerDate.put(date, avgCost);
        }

        return avgCostPerSharePerDate;
    }

}
