package com.example.portfolio.model;

import lombok.Data;

@Data
public class GoogleSheetModal {
    private String date;
    private String ticker;
    private String orderType;
    private long amountInvestedPln;
    private long amountInvestedUsd;
    private long pricePerShareUsd;
    private long quantityBought;
    private long netInvestedAfterCostsUSD;
    private long averageCostBasisPerShareUsd;
    private long fxFeesUsd;
    private long tradingFeesUsd;
    private long ibkrFxRatePlnUsd;
    private long interBankFxRatePlnUsd;
    private long cumulativeSharesHeld;
    private long cumulativeInvestedUsd;
    private long cumulativeInvestedPln;

}
