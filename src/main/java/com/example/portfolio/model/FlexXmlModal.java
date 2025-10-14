package com.example.portfolio.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "FlexQueryResponse")
public class FlexXmlModal {

    @JacksonXmlElementWrapper(localName = "FlexStatements")
    @JacksonXmlProperty(localName = "FlexStatement")
    List<FlexStatement> flexStatements;

    public static class FlexStatement{

        @JacksonXmlElementWrapper(localName = "Trades")
        @JacksonXmlProperty(localName = "Order")
        List<Order> orders;

        @JacksonXmlElementWrapper(localName = "Trades")
        @JacksonXmlProperty(localName = "Trade")
        List<Trade> trades;

    }

    public static class Order{

        private String tradeDate;
        private String symbol;
        private String assetCategory;
        private double quantity;
        private double tradeMoney;
        private double tradePrice;
        private double netCash;
        private double ibCommission;

    }

    public static class Trade{

        private String tradeDate;
        private String symbol;
        private String assetCategory;
        private double quantity;
        private double tradeMoney;
        private double tradePrice;
        private double netCash;
        private double ibCommission;

    }


}

