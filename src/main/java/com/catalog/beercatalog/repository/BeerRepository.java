package com.catalog.beercatalog.repository;

import java.math.BigDecimal;
import java.util.List;

import com.catalog.beercatalog.entity.Beer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BeerRepository extends JpaRepository<Beer, Long>{
    
    @Query(
        value = "SELECT * FROM beers b "
        + "JOIN manufacturers m ON m.id = b.manufacturer_id " 
        + "WHERE (COALESCE(:name) IS NULL OR b.name ILIKE '%' || CAST(:name as VARCHAR) || '%') " 
        + "AND (COALESCE(:type) IS NULL OR b.type ILIKE '%' || CAST(:type as VARCHAR) || '%') "
        + "AND (COALESCE(:desc) IS NULL OR b.description ILIKE '%' || CAST(:desc as VARCHAR) || '%') "
        + "AND (COALESCE(:abv_gt) IS NULL OR b.abv >= CAST(CAST(:abv_gt AS TEXT) AS NUMERIC)) "
        + "AND (COALESCE(:abv_lt) IS NULL OR b.abv <= CAST(CAST(:abv_lt AS TEXT) AS NUMERIC)) "
        + "AND (COALESCE(:manufacturer_id) IS NULL OR b.manufacturer_id = CAST(CAST(:manufacturer_id AS TEXT) AS BIGINT)) "
        + "AND (COALESCE(:manufacturer_name) IS NULL OR m.name ILIKE '%' || CAST(:manufacturer_name as VARCHAR) || '%') "
        + "AND (COALESCE(:manufacturer_nation) IS NULL OR m.nationality ILIKE '%' || CAST(:manufacturer_nation as VARCHAR) || '%') ",
        countQuery = "SELECT count(*) FROM beers",
        nativeQuery = true
    )
    Page<Beer> findAllBeersWithPaginationAndSorting(
        @Param("name") String name,
        @Param("type") String type,
        @Param("desc") String desc,
        @Param("abv_gt") BigDecimal abvGt,
        @Param("abv_lt") BigDecimal abvLt,
        @Param("manufacturer_id") Long manufacturerId,
        @Param("manufacturer_name") String manufacturerName,
        @Param("manufacturer_nation") String manufacturerNationality,
        Pageable pageable);

    List<Beer> findAllByManufacturer_Id(Long id);
    
    Long countAllByName(String name);

    @Query(
        value = "SELECT count(b.id) FROM beers b " 
        + "WHERE b.name = CAST(:name as VARCHAR) " 
        + "AND b.id != CAST(:id as INTEGER)",
        nativeQuery = true
    )
    Long countByNameExcludingId(@Param("name") String name, @Param("id") Long id);
}
