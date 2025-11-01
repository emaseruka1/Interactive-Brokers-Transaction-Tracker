package com.example.portfolio.service.connection;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class NbpExchangeRateConnectionService {

    public NbpExchangeRateConnectionService(){}

    public BigDecimal getNbpInterbankPlnUsdRate(String date) {

        date = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6);
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
            return BigDecimal.ZERO;
        }
    }
}
