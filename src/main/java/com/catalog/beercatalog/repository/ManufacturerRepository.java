package com.catalog.beercatalog.repository;

import com.catalog.beercatalog.entity.Manufacturer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ManufacturerRepository extends JpaRepository<Manufacturer,Long>{
    
    @Query(
        value = "SELECT * FROM manufacturers m "
        + "WHERE (COALESCE(:name) IS NULL OR m.name ILIKE '%' || CAST(:name as VARCHAR) || '%') " 
        + "AND (COALESCE(:nationality) IS NULL OR m.nationality ILIKE '%' || CAST(:nationality as VARCHAR) || '%') ",
        countQuery = "SELECT count(*) FROM manufacturers",
        nativeQuery = true
    )
    Page<Manufacturer> findAllManufacturersWithPaginationAndSorting(
        @Param("name") String name,
        @Param("nationality") String nationality,
        Pageable pageable);

    Manufacturer getByName(String name);

    Manufacturer getByNationality(String nationality);

    Long countAllByName(String name);

    @Query(
        value = "SELECT count(m.id) FROM manufacturers m " 
        + "WHERE m.name = CAST(:name as VARCHAR) " 
        + "AND m.id != CAST(:id as INTEGER)",
        nativeQuery = true
    )
    Long countByNameExcludingId(@Param("name") String name, @Param("id") Long id);
}

