package com.stock.predictioin.controller.user;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class User {

    @Autowired
    private ChatModel chatModel;

    @GetMapping("/stock/{stock}")
    public ResponseEntity<?> prediction(@PathVariable String stock){
        String response = chatModel.call("who are you");
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
