package com.catalog.beercatalog.repository;

import com.catalog.beercatalog.entity.Provider;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;

public interface ProviderRepository extends JpaRepository<Provider, Long>{
    
    @EntityGraph(value = "Provider.related", type = EntityGraphType.LOAD)
    Provider findByEmail(String email);
}
