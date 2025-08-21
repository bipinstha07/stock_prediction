package com.stock.predictioin.service;

import com.stock.predictioin.dto.CompanyStockPriceDto;
import com.stock.predictioin.dto.NewsStatementDto;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@Getter
@Setter
public class StockAPI {

    @Value("${stock.api.key}")
    private String apiKey;

    @Autowired
    private ChatModel chatModel;

    public List<String> getStockPrice(String code) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.alphavantage.co/query?function=TIME_SERIES_WEEKLY&symbol=" + code + "&apikey=" + apiKey))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject obj = new JSONObject(response.body());
        JSONObject weeklyData = obj.getJSONObject("Weekly Time Series");

        PriorityQueue<String> dates = new PriorityQueue<>((a, b) -> b.compareTo(a));
        Iterator<String> keys = weeklyData.keys();
        while (keys.hasNext()) dates.add(keys.next());

        List<String> result = new ArrayList<>();
        for (int i = 0; i < 12 && !dates.isEmpty(); i++) {
            String dateStr = dates.poll();
            double close = Math.round(weeklyData.getJSONObject(dateStr).getDouble("4. close") * 100.0) / 100.0;

            result.add("{date=" + dateStr + ", price=" + close + "}");
        }
        return result;
    }

    @Async
    public CompletableFuture<List<Map<String, Object>>> simulateStock(String stock, NewsStatementDto request) {
        return CompletableFuture.supplyAsync(() -> {
            List<Map<String, Object>> predictions = new ArrayList<>();
            try {
                StringBuilder news = new StringBuilder();
                news.append("Simulate realistic weekly closing prices for ")
                        .append(stock) .append(" for the next ")
                        .append(request.getMonths()).append(" months (~")
                        .append(request.getMonths() * 4).append(" weeks).\n")
                        .append("Today is ").append(LocalDate.now()).append(".\n")
                        .append("Start predictions from the last real closing price below.\n")
                        .append("Real historical data (latest first):\n");


                // 1. Add client-provided history
                request.getStockPriceHistory().forEach(h -> news.append("{").append(h.getDate()).append(":") .append(h.getPrice()).append("}\n") );
                System.out.println("Hi I am here ");
                System.out.println(request.getStockPriceHistory());


                // 2. Add news
                news.append("\nNow generate future weekly closing prices that follow the trend and react slightly to the news below.\n")
                        .append("Strictly output in format {YYYY-MM-DD:price}, one per line. No explanation.\n\n")
                        .append("News statements:\n")
                        .append("When predicting, do not just make a straight slope." + "Introduce realistic short-term fluctuations (weekly ups and downs) while keeping the overall long-term trend. \n" + "Some weeks should dip below the last price, and some should rise above, similar to real stock behavior. \n" + "Prices must remain within a realistic range compared to the historical data.\n") ;

                    request.getNews().forEach(statement -> news.append(statement).append("\n"));
                // Call AI safely

                String response;
                try {
                    response = chatModel.call(news.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    response = ""; // fallback
                }

                for (String line : response.split("\\r?\\n")) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.replace("{", "").replace("}", "").split(":");
                    if (parts.length == 2) {
                        Map<String, Object> entry = new HashMap<>();
                        entry.put("date", parts[0].trim());
                        entry.put("price", Double.parseDouble(parts[1].trim()));
                        predictions.add(entry);
                    }
                }

                System.out.println("Predictions ready: " + predictions);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return predictions;
        });
    }

}
