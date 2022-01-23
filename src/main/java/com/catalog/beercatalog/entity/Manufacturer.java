package com.catalog.beercatalog.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "manufacturers")
@Data
// @NamedEntityGraph(name = "Manufacturer.beers",
//     attributeNodes = @NamedAttributeNode("beers")
// )
public class Manufacturer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String nationality;

    public Manufacturer() {}

    public Manufacturer(String name, String nationality, List<Beer> beers) {
        this.name = name;
        this.nationality = nationality;
    }
    
}
