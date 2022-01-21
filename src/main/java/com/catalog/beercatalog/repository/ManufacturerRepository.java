package com.catalog.beercatalog.repository;

import com.catalog.beercatalog.entity.Manufacturer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerRepository extends JpaRepository<Manufacturer,Long>{
    
}
