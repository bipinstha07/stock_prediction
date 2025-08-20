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

    public List<CompanyStockPriceDto> getStockPrice(String code) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.alphavantage.co/query?function=TIME_SERIES_WEEKLY&symbol=" + code + "&apikey=" + apiKey))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject obj = new JSONObject(response.body());
        JSONObject weeklyData = obj.getJSONObject("Weekly Time Series");

        // PriorityQueue for latest dates
        PriorityQueue<String> dates = new PriorityQueue<>((a, b) -> b.compareTo(a));
        Iterator<String> keys = weeklyData.keys();
        while (keys.hasNext()) dates.add(keys.next());

        List<CompanyStockPriceDto> stockPrices = new ArrayList<>();

        for (int i = 0; i < 12 && !dates.isEmpty(); i++) {
            String dateStr = dates.poll();
            double close = Math.round(weeklyData.getJSONObject(dateStr).getDouble("4. close") * 100.0) / 100.0;

            CompanyStockPriceDto dto = new CompanyStockPriceDto();
            dto.setClosingPrice(String.valueOf(close));
            dto.setDate(java.sql.Date.valueOf(dateStr)); // parse yyyy-MM-dd

            stockPrices.add(dto);
        }

        return stockPrices;
    }


}
