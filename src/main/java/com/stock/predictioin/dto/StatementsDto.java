package com.stock.predictioin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatementsDto {

    private String id;
    private String news;
    private String CompanyName;
    private List<CompanyDto> companyDto;

}
