package com.stock.predictioin.controller.user;

import com.stock.predictioin.dto.CompanyDto;
import com.stock.predictioin.dto.CompanyReq;
import com.stock.predictioin.service.CompanyImp;
import com.stock.predictioin.service.StatementImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
public class CompanyController {

    private CompanyImp companyImp;
    private StatementImp statementImp;

    public CompanyController(CompanyImp companyImp, StatementImp statementImp) {
        this.companyImp = companyImp;
        this.statementImp = statementImp;
    }

    @PostMapping("/add")
    public ResponseEntity<CompanyDto> addCompany(@RequestBody CompanyReq company){
       CompanyDto companyDto = companyImp.addCompany(company.getCompanyName(),company.getCompanyCode());
        return new ResponseEntity<>(companyDto, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<CompanyDto>> getAllComapny(){
        List<CompanyDto> companyDtos = companyImp.getAll();
        return  new ResponseEntity<>(companyDtos,HttpStatus.OK);
    }


}
