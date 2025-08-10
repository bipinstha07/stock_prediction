package com.stock.predictioin.service;

import com.stock.predictioin.dto.StatementsDto;
import com.stock.predictioin.entity.Company;
import com.stock.predictioin.entity.Statements;
import com.stock.predictioin.repository.CompanyRepo;
import com.stock.predictioin.repository.StatementsRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StatementImp {

    private StatementsRepo statementsRepo;
    private CompanyRepo companyRepo;
    private ModelMapper modelMapper;


    public StatementImp(CompanyRepo companyRepo, ModelMapper modelMapper, StatementsRepo statementsRepo) {
        this.companyRepo = companyRepo;
        this.modelMapper = modelMapper;
        this.statementsRepo = statementsRepo;
    }

    public void addStatement(String statement, String stock){
        List<Statements> lists = new ArrayList<>();

        Statements statements = new Statements();
        statements.setId(UUID.randomUUID().toString());
        statements.setNews(statement);
        lists.add(statements);

        Company company = companyRepo.findByName(stock);
        statements.setCName(company.getName());

        statements.setCompany(company);
        company.setStatements(lists);

        companyRepo.save(company);
    }

    public List<StatementsDto> getStatementByStockName(String stock){
        Company company = companyRepo.findByName(stock);
        List<Statements> statements = company.getStatements();
        return statements.stream().map((statements1 -> modelMapper.map(statements1,StatementsDto.class))).toList();
    }

}
