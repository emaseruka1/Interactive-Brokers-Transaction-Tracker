package com.example.portfolio.model;

import java.util.Date;

public class Trade {

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
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public long getAmountInvestedPln() {
        return amountInvestedPln;
    }

    public void setAmountInvestedPln(long amountInvestedPln) {
        this.amountInvestedPln = amountInvestedPln;
    }

    public long getAmountInvestedUsd() {
        return amountInvestedUsd;
    }

    public void setAmountInvestedUsd(long amountInvestedUsd) {
        this.amountInvestedUsd = amountInvestedUsd;
    }

    public long getPricePerShareUsd() {
        return pricePerShareUsd;
    }

    public void setPricePerShareUsd(long pricePerShareUsd) {
        this.pricePerShareUsd = pricePerShareUsd;
    }

    public long getQuantityBought() {
        return quantityBought;
    }

    public void setQuantityBought(long quantityBought) {
        this.quantityBought = quantityBought;
    }

    public long getNetInvestedAfterCostsUSD() {
        return netInvestedAfterCostsUSD;
    }

    public void setNetInvestedAfterCostsUSD(long netInvestedAfterCostsUSD) {
        this.netInvestedAfterCostsUSD = netInvestedAfterCostsUSD;
    }

    public long getAverageCostBasisPerShareUsd() {
        return averageCostBasisPerShareUsd;
    }

    public void setAverageCostBasisPerShareUsd(long averageCostBasisPerShareUsd) {
        this.averageCostBasisPerShareUsd = averageCostBasisPerShareUsd;
    }

    public long getFxFeesUsd() {
        return fxFeesUsd;
    }

    public void setFxFeesUsd(long fxFeesUsd) {
        this.fxFeesUsd = fxFeesUsd;
    }

    public long getTradingFeesUsd() {
        return tradingFeesUsd;
    }

    public void setTradingFeesUsd(long tradingFeesUsd) {
        this.tradingFeesUsd = tradingFeesUsd;
    }

    public long getIbkrFxRatePlnUsd() {
        return ibkrFxRatePlnUsd;
    }

    public void setIbkrFxRatePlnUsd(long ibkrFxRatePlnUsd) {
        this.ibkrFxRatePlnUsd = ibkrFxRatePlnUsd;
    }

    public long getInterBankFxRatePlnUsd() {
        return interBankFxRatePlnUsd;
    }

    public void setInterBankFxRatePlnUsd(long interBankFxRatePlnUsd) {
        this.interBankFxRatePlnUsd = interBankFxRatePlnUsd;
    }

    public long getCumulativeSharesHeld() {
        return cumulativeSharesHeld;
    }

    public void setCumulativeSharesHeld(long cumulativeSharesHeld) {
        this.cumulativeSharesHeld = cumulativeSharesHeld;
    }

    public long getCumulativeInvestedUsd() {
        return cumulativeInvestedUsd;
    }

    public void setCumulativeInvestedUsd(long cumulativeInvestedUsd) {
        this.cumulativeInvestedUsd = cumulativeInvestedUsd;
    }

    public long getCumulativeInvestedPln() {
        return cumulativeInvestedPln;
    }

    public void setCumulativeInvestedPln(long cumulativeInvestedPln) {
        this.cumulativeInvestedPln = cumulativeInvestedPln;
    }
}
