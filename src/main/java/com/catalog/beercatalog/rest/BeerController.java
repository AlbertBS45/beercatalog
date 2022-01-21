package com.catalog.beercatalog.rest;

import java.math.BigDecimal;
import java.util.List;

import com.catalog.beercatalog.entity.Beer;
import com.catalog.beercatalog.service.BeerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/beers")
public class BeerController {
    
    @Autowired
    private BeerService beerSv;

    @GetMapping()
    public List<Beer> findAll(
        @RequestParam(required = false, name = "name") String name, 
        @RequestParam(required = false, name = "type") String type,
        @RequestParam(required = false, name = "abv_gt") BigDecimal abvGt,
        @RequestParam(required = false, name = "abv_lt") BigDecimal abvLt,
        @RequestParam(required = false, name = "manufacturer_id") Long manufacturerId,
        @RequestParam(required = false, name = "manufacturer_name") String manufacturerName,
        @RequestParam(required = false, name = "manufacturer_nationality") String manufacturerNationality,
        @RequestParam(defaultValue = "0") Integer pageNum, 
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id,asc") String[] sort)
    {
        return beerSv.findAll(
            name, 
            type, 
            abvGt, 
            abvLt, 
            manufacturerId, 
            manufacturerName, 
            manufacturerNationality, 
            pageNum, 
            pageSize, 
            sort);
    }

    @GetMapping("/{id}")
    public Beer findById(@PathVariable Long id) {

        Beer beer = beerSv.findById(id);

        if (beer == null) {
            //TODO exception handling
        }

        return beer;
    }

    @PostMapping()
    public Beer addBeer(@RequestBody Beer beer) {

        // In case the id its defined we set it to 0 to manually force the insert
        beer.setId(Long.valueOf(0));

        return beerSv.save(beer);
    }

    @PutMapping()
    public Beer updateBeer(@RequestBody Beer beer) {
        return beerSv.save(beer);
    }

    @DeleteMapping("/{id}")
    public String deleteBeer(@PathVariable Long id) {

        Beer beerToDelete = beerSv.findById(id);

        if (beerToDelete == null) {
           //TODO exception handling
        }

        beerSv.deleteById(id);

        return "Beer deleted with id: " + id;
    }
}
