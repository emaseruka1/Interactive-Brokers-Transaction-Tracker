package com.example.portfolio.service.filter;

import com.example.portfolio.model.IbkrFlexJsonDataModal;
import com.example.portfolio.service.connection.GoogleSheetConnectionService;
import com.example.portfolio.utils.DateUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DateFilter {

    private final IbkrFlexJsonDataModal ibkrFlexJsonDataModal;
    private final GoogleSheetConnectionService googleSheetConnectionService;
    private final List<LocalDate> googleSheetDatesAvailable;
    private final LocalDate googleSheetLatestDateAvailable;
    private static final Logger log = LoggerFactory.getLogger(DateFilter.class);

    @Autowired
    public DateFilter(IbkrFlexJsonDataModal ibkrFlexJsonDataModal,
                      GoogleSheetConnectionService googleSheetConnectionService){

        this.ibkrFlexJsonDataModal = ibkrFlexJsonDataModal;
        this.googleSheetConnectionService = googleSheetConnectionService;

        try {
            this.googleSheetDatesAvailable = this.googleSheetConnectionService.getDatesOfPastTransactionsOnGoogleSheets();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.googleSheetLatestDateAvailable = googleSheetDatesAvailable.stream()
                .max(LocalDate::compareTo)
                .orElse(null);

    }

    public List<JsonNode> filterAllOrdersByDate(){

        log.info("Filtering out Past Dates");

        List<JsonNode> allOrdersFlattened = new ArrayList<>();
        List<JsonNode> ordersFilteredByDate = new ArrayList<>();

        for (JsonNode ord:ibkrFlexJsonDataModal.getAllOrders()){

            if (ord.isObject()){

                allOrdersFlattened.add(ord);

            } else if (ord.isArray()) {

                for (JsonNode node:ord){

                    allOrdersFlattened.add(node);
                }
            }
        }

        log.info("Number of Orders: {}",allOrdersFlattened.size());
        log.info("Number of Orders: {}",allOrdersFlattened);

        for (JsonNode jsonNode : allOrdersFlattened) {

            String orderTradeDateStr = (jsonNode.get("tradeDate")).asText();

            LocalDate orderTradeDate = DateUtils.stringToLocalDate(orderTradeDateStr);

            if (googleSheetDatesAvailable.isEmpty() || orderTradeDate.isAfter(googleSheetLatestDateAvailable)) {
                    ordersFilteredByDate.add(jsonNode);
                }
            }

        return ordersFilteredByDate;
    }

    public List<JsonNode> filterAllTradesByDate(){

        List<JsonNode> allTradesFlattened = new ArrayList<>();
        List<JsonNode> tradesFilteredByDate = new ArrayList<>();

        for (JsonNode trd : ibkrFlexJsonDataModal.getAllTrades()){

            if (trd.isObject()){

                allTradesFlattened.add(trd);

            } else if (trd.isArray()) {

                for (JsonNode node : trd){

                    allTradesFlattened.add(node);
                }
            }
        }

        for (JsonNode jsonNode : allTradesFlattened) {

            String tradeTradeDateStr = (jsonNode.get("tradeDate")).asText();

            LocalDate tradeTradeDate = DateUtils.stringToLocalDate(tradeTradeDateStr);

            if (googleSheetDatesAvailable.isEmpty() || tradeTradeDate.isAfter(googleSheetLatestDateAvailable)){
                tradesFilteredByDate.add(jsonNode);
            }
        }

        return tradesFilteredByDate;
    }
}
