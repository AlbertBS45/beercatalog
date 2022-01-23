package com.catalog.beercatalog.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

    public Manufacturer() {}

    public Manufacturer(String name, String nationality) {
        this.name = name;
        this.nationality = nationality;
    }
    
}
