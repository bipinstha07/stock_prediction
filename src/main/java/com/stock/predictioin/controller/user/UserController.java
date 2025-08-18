package com.stock.predictioin.controller.user;

import com.stock.predictioin.dto.NewsStatementDto;
import com.stock.predictioin.dto.UserDto;
import com.stock.predictioin.service.UserImp;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ChatModel chatModel;

    @Autowired
    private UserImp userImp;

    @PostMapping("/stock/{stock}")
    public ResponseEntity<?> prediction(@PathVariable String stock, @RequestBody NewsStatementDto request){
        StringBuilder news = new StringBuilder();
        news.append("Simulate a hypothetical the weekly closing price for ")
                .append(stock)
                .append(" for the next ")
                .append(request.getMonths())
                .append(" months, starting from the upcoming week.\n").append("Today is ").append(LocalDate.now()).append("Also show today current price too")
                .append("Provide one entry per week, for a total of ")
                .append(request.getMonths() * 4) // assuming ~4 weeks/month
                .append(" entries.\n")
                .append("Strictly respond in this exact format: {YYYY-MM-DD:price}, one per line.\n")
                .append("No explanations, no extra text, only the list.\n\n")
                .append("News statements:\n");


        request.getNews().forEach(statement ->
                news.append(statement).append("\n")
        );
        System.out.println("Before Calling model");
        String response = chatModel.call(news.toString());

        List<Map<String, Object>> list = new ArrayList<>();
        for (String line : response.split("\\r?\\n")) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(":");
            if (parts.length == 2) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("date", parts[0].trim());
                entry.put("price", Double.parseDouble(parts[1].trim()));
                list.add(entry);
            }
        }
        System.out.println("Testing ");
        list.forEach((a)-> System.out.println(a));
        return new ResponseEntity<>(list, HttpStatus.OK);

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
