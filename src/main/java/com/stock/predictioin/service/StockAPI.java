package com.stock.predictioin.service;

import com.stock.predictioin.dto.CompanyStockPriceDto;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

@Service
@Getter
@Setter
public class StockAPI {

    @Value("${stock.api.key}")
    private String apiKey;

    public List<String> getStockPrice(String code) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.alphavantage.co/query?function=TIME_SERIES_WEEKLY&symbol=" + code + "&apikey=" + apiKey))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        JSONObject obj = new JSONObject(response.body());
        JSONObject weeklyData = obj.getJSONObject("Weekly Time Series");

        PriorityQueue<String> dates = new PriorityQueue<>((a, b) -> b.compareTo(a));
        Iterator<String> keys = weeklyData.keys();
        while (keys.hasNext()) dates.add(keys.next());

        List<String> result = new ArrayList<>();
        System.out.println("here list result");
        for (int i = 0; i < 12 && !dates.isEmpty(); i++) {
            String dateStr = dates.poll();
            double close = Math.round(weeklyData.getJSONObject(dateStr).getDouble("4. close") * 100.0) / 100.0;

            result.add("{date=" + dateStr + ", price=" + close + "}");
        }
        System.out.println(result);
        return result;
    }



}
