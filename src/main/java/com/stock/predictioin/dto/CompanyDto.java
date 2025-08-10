package com.stock.predictioin.dto;

import com.stock.predictioin.entity.Statements;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {

    private String id;
    private String name;
    private StatementsDto statementsDto;
}
