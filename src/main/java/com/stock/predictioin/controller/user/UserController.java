package com.stock.predictioin.controller.user;

import com.stock.predictioin.dto.CompanyStockPriceDto;
import com.stock.predictioin.dto.NewsStatementDto;
import com.stock.predictioin.dto.UserDto;
import com.stock.predictioin.service.StockAPI;
import com.stock.predictioin.service.UserImp;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ChatModel chatModel;

    @Autowired
    private UserImp userImp;

    @Autowired
    private StockAPI stockAPI;

    @PostMapping("/stock/{stock}")
    public ResponseEntity<?> prediction(@PathVariable String stock, @RequestBody NewsStatementDto request) {
        StringBuilder news = new StringBuilder();
        news.append("Simulate realistic weekly closing prices for ")
                .append(stock)
                .append(" for the next ")
                .append(request.getMonths()).append(" months (~")
                .append(request.getMonths() * 4).append(" weeks).\n")
                .append("Today is ").append(LocalDate.now()).append(".\n")
                .append("Start predictions from the last real closing price below.\n")
                .append("Real historical data (latest first):\n");

        // 1. Add client-provided history
        request.getStockPriceHistory().forEach(h ->
                news.append("{").append(h.getDate()).append(":")
                        .append(h.getPrice()).append("}\n")
        );

        System.out.println("Hi I am here ");
        System.out.println(request.getStockPriceHistory());

        // 2. Add news
        news.append("\nNow generate future weekly closing prices that follow the trend and react slightly to the news below.\n")
                .append("Strictly output in format {YYYY-MM-DD:price}, one per line. No explanation.\n\n")
                .append("News statements:\n")
                .append("When predicting, do not just make a straight slope." +
                        "Introduce realistic short-term fluctuations (weekly ups and downs) while keeping the overall long-term trend. \n" +
                        "Some weeks should dip below the last price, and some should rise above, similar to real stock behavior. \n" +
                        "Prices must remain within a realistic range compared to the historical data.\n")
        ;

        request.getNews().forEach(statement -> news.append(statement).append("\n"));

        // 3. Call AI
        String response = chatModel.call(news.toString());

        // 4. Parse response
        List<Map<String, Object>> predictions = new ArrayList<>();
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

        // 5. Return predictions only (or merge with history if you want)
        return new ResponseEntity<>(predictions, HttpStatus.OK);
    }


    @GetMapping("/stock/getByCode/{code}")
    public ResponseEntity<?> getStock(@PathVariable String code) throws IOException, InterruptedException {
        System.out.println("Get Price by Code");
        return new ResponseEntity<>(stockAPI.getStockPrice(code),HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody UserDto userDto){
        return new ResponseEntity<>(userImp.createUser(userDto),HttpStatus.CREATED);
    }

    @GetMapping("/getUser/{email}")
    public ResponseEntity<UserDto> createAccount(@PathVariable String email){
        return new ResponseEntity<>(userImp.getUser(email),HttpStatus.OK);
    }


}
