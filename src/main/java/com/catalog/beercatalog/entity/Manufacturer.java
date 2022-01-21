package com.catalog.beercatalog.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "manufacturers")
@Data
public class Manufacturer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String nationality;

    @JsonIgnore
    @OneToMany(mappedBy = "manufacturer")
    private List<Beer> beers;

    public Manufacturer() {}

    public Manufacturer(String name, String nationality, List<Beer> beers) {
        this.name = name;
        this.nationality = nationality;
        this.beers = beers;
    }
    
}
