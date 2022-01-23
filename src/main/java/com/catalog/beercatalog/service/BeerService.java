package com.catalog.beercatalog.service;

import java.math.BigDecimal;
import java.util.Optional;

import com.catalog.beercatalog.entity.Beer;
import com.catalog.beercatalog.repository.BeerRepository;
import com.catalog.beercatalog.utils.ServiceUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class BeerService {

    @Autowired
    private BeerRepository beerRepo;

    @Transactional(readOnly = true)
    public Page<Beer> findAll(
        String name, 
        String type, 
        BigDecimal abvGt,
        BigDecimal abvLt,
        Long manufacturerId,
        String manufacturerName,
        String manufacturerNationality,
        Integer pageNum, 
        Integer pageSize, 
        String sort[]) 
    {

        // Setting the correct format for pageNum
        pageNum--;

        // Making the request pageable and sortable
        Pageable paging = ServiceUtil.generatePagingAndSorting(pageNum, pageSize, sort);
        
        // Setting params values for query
        String beerParamName = (StringUtils.hasText(name) ? name : null);
        String beerParamType = (StringUtils.hasText(type) ? type : null);
        BigDecimal beerParamAbvGt = abvGt != null ? abvGt : null;
        BigDecimal beerParamAbvLt = abvLt != null ? abvLt : null;
        Long beerManufacterId = manufacturerId != null ? manufacturerId : null;
        String beerManufacterName = (StringUtils.hasText(manufacturerName) ? manufacturerName : null);
        String beerManufacterNationality = (StringUtils.hasText(manufacturerNationality) ? manufacturerNationality : null);

        // Executing query
        Page<Beer> pagedResult = beerRepo.findAllBeersWithPaginationAndSorting(
            beerParamName, 
            beerParamType, 
            beerParamAbvGt, 
            beerParamAbvLt, 
            beerManufacterId,
            beerManufacterName,
            beerManufacterNationality,
            paging);
        
        return pagedResult;
    }

    @Transactional(readOnly = true)
    public Beer findById(Long id) {

        Optional<Beer> beerOpt = beerRepo.findById(id);
        
        Beer beer = null;

        if (beerOpt.isPresent()) {
            beer = beerOpt.get();
        }

        return beer;
    }
    
    @Transactional()
    public Beer save(Beer beer) {

        return beerRepo.save(beer);
    }

    @Transactional()
    public void deleteById(Long id) {
        beerRepo.deleteById(id);
    }
    
}
