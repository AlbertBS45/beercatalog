package com.catalog.beercatalog.entity;

import java.util.List;

import javax.persistence.CascadeType;
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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


// Our custom user table
@Entity
@NamedEntityGraphs({
    @NamedEntityGraph(name="Provider.related", attributeNodes={
        @NamedAttributeNode("authorities"),
    })
})
@Table(name = "providers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Provider {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String pwd;

    @OneToOne()
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @JsonIgnore
    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Authority> authorities;

    public Provider(String email, String pwd, Manufacturer manufacturer, List<Authority> authorities) {
        this.email = email;
        this.pwd = pwd;
        this.manufacturer = manufacturer;
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "Provider [email=" + email + ", id=" + id + ", manufacturer=" + manufacturer + "]";
    }
    
}
