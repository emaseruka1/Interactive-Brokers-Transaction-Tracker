package com.example.portfolio.service.mapper;

import com.example.portfolio.model.GoogleSheetModal;
import com.example.portfolio.service.connection.GoogleSheetConnectionService;
import com.example.portfolio.service.filter.AssetSymbolFilter;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlexAndGoogleSheetModalsMapper {

    private final GoogleSheetModal googleSheetModal;
    private final AssetSymbolFilter assetSymbolFilter;
    private final GoogleSheetConnectionService googleSheetConnectionService;

    @Autowired
    public FlexAndGoogleSheetModalsMapper(GoogleSheetModal googleSheetModal,AssetSymbolFilter assetSymbolFilter, GoogleSheetConnectionService googleSheetConnectionService){

        this.googleSheetModal = googleSheetModal;
        this.assetSymbolFilter = assetSymbolFilter;
        this.googleSheetConnectionService = googleSheetConnectionService;
    }

    public void createTransactionForGoogleSheets(){

        List<JsonNode> stockOrders = assetSymbolFilter.getStockOrders();



        for (JsonNode ord:stockOrders){

            List<Object> transaction = new ArrayList<>();

            googleSheetModal.setDate(ord.get("tradeDate").asText());
            googleSheetModal.setTicker(ord.get("symbol").asText());
            googleSheetModal.setOrderType("buy");
            googleSheetModal.setAmountInvestedPln(BigDecimal.ZERO);
            googleSheetModal.setAmountInvestedUsd(new BigDecimal(ord.get("tradeMoney").asText()));
            googleSheetModal.setPricePerShareUsd(new BigDecimal(ord.get("tradePrice").asText()));
            googleSheetModal.setQuantityBought(new BigDecimal(ord.get("quantity").asText()));
            googleSheetModal.setNetInvestedAfterCostsUSD(new BigDecimal(ord.get("netCash").asText()).abs());
            googleSheetModal.setAverageCostBasisPerShareUsd(BigDecimal.ZERO);
            googleSheetModal.setFxFeesUsd(BigDecimal.ZERO);
            googleSheetModal.setTradingFeesUsd(new BigDecimal(ord.get("ibCommission").asText()).abs());
            googleSheetModal.setIbkrFxRatePlnUsd(new BigDecimal(ord.get("fxRateToBase").asText()));
            googleSheetModal.setInterBankFxRatePlnUsd(BigDecimal.ZERO);
            googleSheetModal.setCumulativeSharesHeld(BigDecimal.ZERO);
            googleSheetModal.setCumulativeInvestedUsd(BigDecimal.ZERO);
            googleSheetModal.setCumulativeInvestedPln(BigDecimal.ZERO);

            transaction.add(googleSheetModal);

            List<Object> rowValues =List.of(
                    googleSheetModal.getDate(),
                    googleSheetModal.getTicker(),
                    googleSheetModal.getOrderType(),
                    googleSheetModal.getAmountInvestedPln(),
                    googleSheetModal.getAmountInvestedUsd(),
                    googleSheetModal.getPricePerShareUsd(),
                    googleSheetModal.getQuantityBought(),
                    googleSheetModal.getNetInvestedAfterCostsUSD(),
                    googleSheetModal.getAverageCostBasisPerShareUsd(),
                    googleSheetModal.getFxFeesUsd(),
                    googleSheetModal.getTradingFeesUsd(),
                    googleSheetModal.getIbkrFxRatePlnUsd(),
                    googleSheetModal.getInterBankFxRatePlnUsd(),
                    googleSheetModal.getCumulativeSharesHeld(),
                    googleSheetModal.getCumulativeInvestedUsd(),
                    googleSheetModal.getCumulativeInvestedPln()
            );

            try {
                googleSheetConnectionService.appendRowValuesToGoogleSheets(rowValues);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
