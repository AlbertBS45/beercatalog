package com.catalog.beercatalog.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "beers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal abv;

    @ManyToOne()
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    public Beer(String name, String type, String description, BigDecimal abv, Manufacturer manufacturer) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.abv = abv;
        this.manufacturer = manufacturer;
    }

    

}

