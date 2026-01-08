package com.example.portfolio.service.connection;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class NbpExchangeRateConnectionService {

    public NbpExchangeRateConnectionService(){}

    public BigDecimal getNbpInterbankPlnUsdRate(String ibkrTradedate) {

        LocalDate dateIterator = LocalDate.parse(ibkrTradedate, DateTimeFormatter.ofPattern("yyyyMMdd"));

        while(true){

            System.out.println("Trying to fetch NBP fx Rate for date: "+dateIterator);
            String date = dateIterator.toString();
            String url = "https://api.nbp.pl/api/exchangerates/rates/A/USD/" + date + "/?format=json";
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

            try{
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                JSONObject json = new JSONObject(response.body());
                JSONArray rates = json.getJSONArray("rates");
                double midRate = rates.getJSONObject(0).getDouble("mid");
                return BigDecimal.valueOf(midRate);

            } catch (Exception e) {

                dateIterator = dateIterator.minusDays(1);
            }
        }
    }
}
