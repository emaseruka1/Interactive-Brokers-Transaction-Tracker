package com.example.portfolio.service.filter;

import com.example.portfolio.model.IbkrFlexJsonDataModal;
import com.example.portfolio.service.connection.GoogleSheetConnectionService;
import com.example.portfolio.utils.DateUtils;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
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

        List<JsonNode> ordersFilteredByDate = new ArrayList<>();

        for (JsonNode ord:ibkrFlexJsonDataModal.getAllOrders()){

            for (int i=0; i<ord.size();i++){

                String orderTradeDateStr = (ord.get(i).get("tradeDate")).asText();

                LocalDate orderTradeDate = DateUtils.stringToLocalDate(orderTradeDateStr);

                if ( googleSheetDatesAvailable.isEmpty() || orderTradeDate.isAfter(googleSheetLatestDateAvailable)){
                    ordersFilteredByDate.add(ord.get(i));
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

                LocalDate tradeTradeDate = DateUtils.stringToLocalDate(tradeTradeDateStr);

                if (tradeTradeDate.isAfter(googleSheetLatestDateAvailable) || googleSheetDatesAvailable.isEmpty()){
                    tradesFilteredByDate.add(trd.get(i));
                }
            }
        }

        return tradesFilteredByDate;
    }
}
