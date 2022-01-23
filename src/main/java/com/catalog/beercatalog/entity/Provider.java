package com.catalog.beercatalog.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;


// Our custom user table named providers
@Entity
@NamedEntityGraphs({
    @NamedEntityGraph(name="Provider.related", attributeNodes={
        @NamedAttributeNode("manufacturer"),
        @NamedAttributeNode("authorities"),
    })
})
@Table(name = "providers")
@Data
public class Provider {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String pwd;

    @Column(nullable = false)
    private String role;

    @OneToOne()
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @JsonIgnore
    @OneToMany(mappedBy = "provider")
    private List<Authority> authorities;

    public Provider(){}

    public Provider(String email, String pwd, String role, Manufacturer manufacturer, List<Authority> authorities) {
        this.email = email;
        this.pwd = pwd;
        this.role = role;
        this.manufacturer = manufacturer;
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "Provider [email=" + email + ", id=" + id + ", pwd=" + pwd + ", role=" + role + "]";
    }

    

    
}
