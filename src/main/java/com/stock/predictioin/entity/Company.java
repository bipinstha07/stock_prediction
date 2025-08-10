package com.stock.predictioin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @Id
    private String id;

    private String name;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Statements> statements;

}
