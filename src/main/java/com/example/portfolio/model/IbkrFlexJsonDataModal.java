package com.example.portfolio.model;

import com.example.portfolio.service.connection.FlexXmlFileConnectionService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class IbkrFlexJsonDataModal {

    private final FlexXmlFileConnectionService flexXmlFileConnectionService;
    private final JsonNode flexIbkrJsonData;
    private final JsonNode flexStatements;
    private final JsonNode flexStatement;
    private final List<JsonNode> trades;
    private final List<JsonNode> allOrders;
    private final List<JsonNode> allTrades;

    @Autowired
    public IbkrFlexJsonDataModal(FlexXmlFileConnectionService flexXmlFileConnectionService){

        this.flexXmlFileConnectionService = flexXmlFileConnectionService;
        this.flexIbkrJsonData = flexXmlFileConnectionService.parseFlexXmlFileToJson();
        this.flexStatements = flexIbkrJsonData.path("FlexStatements");
        this.flexStatement = flexStatements.path("FlexStatement");
        this.trades = iterateThroughStatementJsonNodesForTradesWithData();
        this.allOrders = iterateThroughTradeJsonNodesForAllOrders();
        this.allTrades = iterateThroughTradeJsonNodesForAllTrades();
    }

    public List<JsonNode> iterateThroughStatementJsonNodesForTradesWithData(){

        List<JsonNode> tradesWithData = new ArrayList<>();

        for (JsonNode stmt:flexStatement){
            if(stmt.path("Trades").size()>0){tradesWithData.add(stmt);}
        }

        return tradesWithData;
    }

    public List<JsonNode> iterateThroughTradeJsonNodesForAllOrders() {
        List<JsonNode> allOrders = new ArrayList<>();

        for (JsonNode trade : trades) {
            JsonNode orderNode = trade.get("Trades").get("Order");
            allOrders.add(orderNode);
        }

        return allOrders;
    }

    public List<JsonNode> iterateThroughTradeJsonNodesForAllTrades(){

        List<JsonNode> allTrades = new ArrayList<>();

        for (JsonNode trade : trades) {
            JsonNode tradeNode = trade.get("Trades").get("Trade");
            allTrades.add(tradeNode);
        }

        return allTrades;
    }

}
