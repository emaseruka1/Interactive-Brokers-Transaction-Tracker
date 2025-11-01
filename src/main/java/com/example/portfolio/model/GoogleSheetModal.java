package com.example.portfolio.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Component
public class GoogleSheetModal {
    private String date;
    private String ticker;
    private String orderType;
    private BigDecimal amountInvestedPln;
    private BigDecimal actualAmountInvestedUsd;
    private BigDecimal pricePerShareUsd;
    private BigDecimal quantityBought;
    private BigDecimal netInvestedAfterCostsUSD;
    private BigDecimal averageCostBasisPerShareUsd;
    private BigDecimal fxFeesUsd;
    private BigDecimal tradingFeesUsd;
    private BigDecimal ibkrFxRatePlnUsd;
    private BigDecimal interBankFxRatePlnUsd;
}
