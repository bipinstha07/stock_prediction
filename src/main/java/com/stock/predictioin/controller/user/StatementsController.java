package com.stock.predictioin.controller.user;

import com.stock.predictioin.dto.StatementsDto;
import com.stock.predictioin.service.StatementImp;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock/statements")
public class StatementsController {


    private StatementImp statementImp;
    private ModelMapper modelMapper;

    public StatementsController(ModelMapper modelMapper, StatementImp statementImp) {
        this.modelMapper = modelMapper;
        this.statementImp = statementImp;
    }

    @PostMapping("/{stock}/add")
    public ResponseEntity<String> addStatement(@PathVariable String stock, @RequestBody String statement){
        statementImp.addStatement(statement,stock);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @GetMapping("/{stock}")
    public ResponseEntity<List<StatementsDto>> getStatementByStock(@PathVariable String stock){
       return new ResponseEntity<>(statementImp.getStatementByStockName(stock),HttpStatus.OK);
    }

}
