package com.example.portfolio.service.mapper;

import com.example.portfolio.model.GoogleSheetModal;
import com.example.portfolio.service.calculation.AvgCostPerShareAggregation;
import com.example.portfolio.service.calculation.IbkrFxRateCalculation;
import com.example.portfolio.service.connection.GoogleSheetConnectionService;
import com.example.portfolio.service.connection.NbpExchangeRateConnectionService;
import com.example.portfolio.service.filter.AssetSymbolFilter;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FlexAndGoogleSheetModalsMapper {

    private final GoogleSheetModal googleSheetModal;
    private final AssetSymbolFilter assetSymbolFilter;
    private final GoogleSheetConnectionService googleSheetConnectionService;
    private final AvgCostPerShareAggregation avgCostPerShareAggregation;
    private final IbkrFxRateCalculation ibkrFxRateCalculation;
    private final NbpExchangeRateConnectionService nbpExchangeRateConnectionService;

    @Autowired
    public FlexAndGoogleSheetModalsMapper(GoogleSheetModal googleSheetModal,
                                          AssetSymbolFilter assetSymbolFilter,
                                          GoogleSheetConnectionService googleSheetConnectionService,
                                          AvgCostPerShareAggregation avgCostPerShareAggregation,
                                          IbkrFxRateCalculation ibkrFxRateCalculation,
                                          NbpExchangeRateConnectionService nbpExchangeRateConnectionService){

        this.googleSheetModal = googleSheetModal;
        this.assetSymbolFilter = assetSymbolFilter;
        this.googleSheetConnectionService = googleSheetConnectionService;
        this.avgCostPerShareAggregation = avgCostPerShareAggregation;
        this.ibkrFxRateCalculation = ibkrFxRateCalculation;
        this.nbpExchangeRateConnectionService = nbpExchangeRateConnectionService;
    }

    public List<List<Object>> createTransactionListForGoogleSheets(){

        List<JsonNode> stockOrders = assetSymbolFilter.getStockOrders();

        Map<String, BigDecimal> avgCostPerSharePerDate = avgCostPerShareAggregation.calculateAvgCostPerSharePerDate();

        Map<String, BigDecimal> usdPlnFxRatePerDate = ibkrFxRateCalculation.calculateUsdPlnFxRatePerDate();

        List<List<Object>> transactionListforGoogleSheets = new ArrayList<>();

        for (JsonNode ord:stockOrders){

            String tradeDate = ord.get("tradeDate").asText();

            BigDecimal interbankFxRate = nbpExchangeRateConnectionService.getNbpInterbankPlnUsdRate(tradeDate);

            BigDecimal ibkrFxRate = usdPlnFxRatePerDate.getOrDefault(tradeDate,BigDecimal.ZERO);

            googleSheetModal.setDate(tradeDate);
            googleSheetModal.setTicker(ord.get("symbol").asText());
            googleSheetModal.setOrderType("buy");
            googleSheetModal.setAmountInvestedPln(BigDecimal.valueOf(3700));
            googleSheetModal.setActualAmountInvestedUsd(new BigDecimal(ord.get("tradeMoney").asText()));
            googleSheetModal.setPricePerShareUsd(new BigDecimal(ord.get("tradePrice").asText()));
            googleSheetModal.setQuantityBought(new BigDecimal(ord.get("quantity").asText()));
            googleSheetModal.setNetInvestedAfterCostsUSD(new BigDecimal(ord.get("netCash").asText()).abs());
            googleSheetModal.setAverageCostBasisPerShareUsd(avgCostPerSharePerDate.getOrDefault(tradeDate,BigDecimal.ZERO));
            googleSheetModal.setFxFeesUsd(ibkrFxRate.subtract(interbankFxRate).divide(interbankFxRate, 6, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
            googleSheetModal.setTradingFeesUsd(new BigDecimal(ord.get("ibCommission").asText()).abs());
            googleSheetModal.setIbkrFxRatePlnUsd(ibkrFxRate);
            googleSheetModal.setInterBankFxRatePlnUsd(interbankFxRate);

            List<Object> rowValues =List.of(
                    googleSheetModal.getDate(),
                    googleSheetModal.getTicker(),
                    googleSheetModal.getOrderType(),
                    googleSheetModal.getAmountInvestedPln(),
                    googleSheetModal.getActualAmountInvestedUsd(),
                    googleSheetModal.getPricePerShareUsd(),
                    googleSheetModal.getQuantityBought(),
                    googleSheetModal.getNetInvestedAfterCostsUSD(),
                    googleSheetModal.getAverageCostBasisPerShareUsd(),
                    googleSheetModal.getFxFeesUsd(),
                    googleSheetModal.getTradingFeesUsd(),
                    googleSheetModal.getIbkrFxRatePlnUsd(),
                    googleSheetModal.getInterBankFxRatePlnUsd()
            );

            transactionListforGoogleSheets.add(rowValues);
        }
        return transactionListforGoogleSheets;
    }

    @PostConstruct
    public void sendTransactionListToGoogleSheets(){

        List<List<Object>> transactionListforGoogleSheets = createTransactionListForGoogleSheets();

        for (List<Object> transaction:transactionListforGoogleSheets) {

            try {
                googleSheetConnectionService.appendRowValuesToGoogleSheets(transaction);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
