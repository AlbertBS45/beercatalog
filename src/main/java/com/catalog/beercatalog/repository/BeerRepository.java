package com.catalog.beercatalog.repository;

import com.catalog.beercatalog.entity.Beer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerRepository extends JpaRepository<Beer, Long>{

}
