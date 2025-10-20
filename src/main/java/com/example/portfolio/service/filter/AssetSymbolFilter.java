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

        List<JsonNode> stockOrdersFilteredByDate = new ArrayList<>();

        List<JsonNode> ordersFilteredByDate = dateFilter.filterAllOrdersByDate();

        for (JsonNode ord:ordersFilteredByDate){

            for (int i=0; i<ord.size();i++){

                String assetCategory = (ord.get(i).get("assetCategory")).asText();


                if (assetCategory.equals("STK")){

                    stockOrdersFilteredByDate.add(ord.get(i));
            }
            }
        }
        return stockOrdersFilteredByDate;
    }

    public List<JsonNode> getStockTrades(){

        List<JsonNode> stockTradesFilteredByDate = new ArrayList<>();

        List<JsonNode> tradesFilteredByDate = dateFilter.filterAllTradesByDate();

        for (JsonNode trd:tradesFilteredByDate){

            for (int i=0; i<trd.size();i++){

                String assetCategory = (trd.get(i).get("assetCategory")).asText();

                if (assetCategory.equals("STK")){

                    stockTradesFilteredByDate.add(trd.get(i));
                }
            }
        }
        return stockTradesFilteredByDate;

    }

    @PostConstruct
    public void init(){
        System.out.println(getStockTrades());
    }

}
