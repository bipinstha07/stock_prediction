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
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/user")
public class UserController {



    @Autowired
    private UserImp userImp;

    @Autowired
    private StockAPI stockAPI;

    @PostMapping("/stock/{stock}")
    public CompletableFuture<ResponseEntity<List<Map<String,Object>>>> prediction(@PathVariable String stock, @RequestBody NewsStatementDto request) {
        return stockAPI.simulateStock(stock, request)
                .thenApply(ResponseEntity::ok);
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
