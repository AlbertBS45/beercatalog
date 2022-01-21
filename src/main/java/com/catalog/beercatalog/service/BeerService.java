package com.catalog.beercatalog.service;

import java.util.List;
import java.util.Optional;

import com.catalog.beercatalog.entity.Beer;
import com.catalog.beercatalog.repository.BeerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeerService {

    @Autowired
    private BeerRepository beerRepo;

    public List<Beer> findAll() {
        return beerRepo.findAll();
    }

    public Beer findById(Long id) {

        Optional<Beer> beerOpt = beerRepo.findById(id);
        
        Beer beer = null;

        if (beerOpt.isPresent()) {
            beer = beerOpt.get();
        }

        return beer;
    }

    public Beer save(Beer beer) {
        Beer beerAddedOrUpdated = beerRepo.save(beer);

        if (beerAddedOrUpdated == null) {
            //TODO exception handling
        }

        return beerAddedOrUpdated;
    }

    public void deleteById(Long id) {
        beerRepo.deleteById(id);
    }
}
