package com.example.portfolio.service.filter;

import com.example.portfolio.model.IbkrFlexJsonDataModal;
import com.example.portfolio.service.connection.GoogleSheetConnectionService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DateFilter {

    private final IbkrFlexJsonDataModal ibkrFlexJsonDataModal;
    private final GoogleSheetConnectionService googleSheetConnectionService;
    private final List<LocalDate> googleSheetDatesAvailable;
    private final LocalDate googleSheetLatestDateAvailable;


//    public List<LocalDate> googleSheetDatesAvailable = List.of(LocalDate.of(2025, 9, 25));

//    public LocalDate googleSheetLatestDateAvailable = googleSheetDatesAvailable.get(0);

    @Autowired
    public DateFilter(IbkrFlexJsonDataModal ibkrFlexJsonDataModal,
                      GoogleSheetConnectionService googleSheetConnectionService){

        this.ibkrFlexJsonDataModal = ibkrFlexJsonDataModal;
        this.googleSheetConnectionService = googleSheetConnectionService;

        this.googleSheetDatesAvailable = this.googleSheetConnectionService.getDatesOfPastTransactionsOnGoogleSheets();
        this.googleSheetLatestDateAvailable = googleSheetDatesAvailable.stream()
                .max(LocalDate::compareTo)
                .orElse(null);

    }

    public List<JsonNode> filterAllOrdersByDate(){

        List<JsonNode> ordersFilteredByDate = new ArrayList<>();

        for (JsonNode ord:ibkrFlexJsonDataModal.getAllOrders()){

            for (int i=0; i<ord.size();i++){

                String orderTradeDateStr = (ord.get(i).get("tradeDate")).asText();

                LocalDate orderTradeDate = LocalDate.parse(orderTradeDateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));

                if ( googleSheetDatesAvailable == null || orderTradeDate.isAfter(googleSheetLatestDateAvailable)){
                    ordersFilteredByDate.add(ord);
                }
            }
        }

        return ordersFilteredByDate;
    }

    public List<JsonNode> filterAllTradesByDate(){

        List<JsonNode> tradesFilteredByDate = new ArrayList<>();

        for (JsonNode trd:ibkrFlexJsonDataModal.getAllTrades()){

            for (int i=0; i<trd.size();i++){

                String tradeTradeDateStr = (trd.get(i).get("tradeDate")).asText();
                LocalDate tradeTradeDate = LocalDate.parse(tradeTradeDateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));

                if (tradeTradeDate.isAfter(googleSheetLatestDateAvailable) || googleSheetDatesAvailable.isEmpty()){
                    tradesFilteredByDate.add(trd);
                }
            }
        }

        return tradesFilteredByDate;
    }

}
