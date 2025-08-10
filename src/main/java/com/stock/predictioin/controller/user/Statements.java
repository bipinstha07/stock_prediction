package com.stock.predictioin.controller.user;

import com.stock.predictioin.service.StatementImp;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock/statements")
public class Statements {


    private StatementImp statementImp;
    private ModelMapper modelMapper;

    @PostMapping("/{stock}/add")
    public ResponseEntity<String> addStatement(){
            statementImp
    }


}
