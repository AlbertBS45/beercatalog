package com.catalog.beercatalog.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "beers")
@Data
public class Beer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    private String description;

    private BigDecimal abv;

    @ManyToOne()
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    public Beer() {}

    public Beer(String name, String type, String description, BigDecimal abv, Manufacturer manufacturer) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.abv = abv;
        this.manufacturer = manufacturer;
    }

    

}

