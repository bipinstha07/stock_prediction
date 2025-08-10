package com.stock.predictioin.service;

import com.stock.predictioin.dto.CompanyDto;
import com.stock.predictioin.entity.Company;
import com.stock.predictioin.repository.CompanyRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyImp {

    private CompanyRepo companyRepo;
    private ModelMapper modelMapper;

    public CompanyImp(CompanyRepo companyRepo, ModelMapper modelMapper) {
        this.companyRepo = companyRepo;
        this.modelMapper = modelMapper;
    }

    public CompanyDto addCompany(String companyName){
        Company company = new Company();
        company.setId(UUID.randomUUID().toString());
        company.setName(companyName);
        Company companySaved = companyRepo.save(company);
        return modelMapper.map(companySaved,CompanyDto.class);
    }


    public void deleteCompany(String companyName){
       Company company = companyRepo.findByName(companyName);
       companyRepo.delete(company);

    }

    public List<CompanyDto> getAll(){
        List<Company> companies = companyRepo.findAll();
        return  companies.stream().map((company -> modelMapper.map(company,CompanyDto.class))).toList();

    }



}
