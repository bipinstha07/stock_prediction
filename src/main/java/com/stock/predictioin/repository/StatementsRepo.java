package com.stock.predictioin.repository;

import com.stock.predictioin.entity.Statements;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatementsRepo extends JpaRepository<Statements,String> {
}
