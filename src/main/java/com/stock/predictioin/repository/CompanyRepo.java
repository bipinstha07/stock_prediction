package com.stock.predictioin.repository;

import com.stock.predictioin.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepo extends JpaRepository<Company,String> {

    Company findByName(String name);
}
