package com.example.portfolio.service.filter;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssetSymbolFilter{

    private final DateFilter dateFilter;

    @Autowired
    public AssetSymbolFilter(DateFilter dateFilter){

        this.dateFilter = dateFilter;
    }

    public List<JsonNode> getStockOrders(){

        List<JsonNode> stockOrders = new ArrayList<>();

        List<JsonNode> ordersFilteredByDate = dateFilter.filterAllOrdersByDate();

        for (JsonNode ord : ordersFilteredByDate) {
            String assetCategory = ord.get("assetCategory").asText();
            if ("STK".equals(assetCategory)) {
                stockOrders.add(ord);
            }
        }

        return stockOrders;
    }

    public List<JsonNode> getStockTrades(){

        List<JsonNode> stockTrades = new ArrayList<>();

        List<JsonNode> tradesFilteredByDate = dateFilter.filterAllTradesByDate();

        for (JsonNode trd:tradesFilteredByDate){

            String assetCategory = (trd.get("assetCategory")).asText();

            if (assetCategory.equals("STK")){
                stockTrades.add(trd);
            }
        }
        return stockTrades;

    }
}
