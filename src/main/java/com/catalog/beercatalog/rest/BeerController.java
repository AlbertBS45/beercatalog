package com.catalog.beercatalog.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.catalog.beercatalog.entity.Beer;
import com.catalog.beercatalog.exception.IdSpecifiedException;
import com.catalog.beercatalog.exception.MissingRequiredsException;
import com.catalog.beercatalog.exception.NameAlreadyExistsException;
import com.catalog.beercatalog.exception.NotFoundException;
import com.catalog.beercatalog.service.BeerService;
import com.catalog.beercatalog.utils.DateFormatUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, Object>> findAll(
        @RequestParam(required = false, name = "name") String name, 
        @RequestParam(required = false, name = "type") String type,
        @RequestParam(required = false, name = "description") String description,
        @RequestParam(required = false, name = "abv_gt") BigDecimal abvGt,
        @RequestParam(required = false, name = "abv_lt") BigDecimal abvLt,
        @RequestParam(required = false, name = "manufacturer_id") Long manufacturerId,
        @RequestParam(required = false, name = "manufacturer_name") String manufacturerName,
        @RequestParam(required = false, name = "manufacturer_nationality") String manufacturerNationality,
        @RequestParam(defaultValue = "1") Integer pageNum, 
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id,asc") String[] sort)
    {

        List<Beer> beers = new ArrayList<Beer>();

        // Get the paged and sorted result
        Page<Beer> pagedResult = beerSv.findAll(
            name, 
            type,
            description, 
            abvGt, 
            abvLt, 
            manufacturerId, 
            manufacturerName, 
            manufacturerNationality, 
            pageNum, 
            pageSize, 
            sort);
        
        beers = pagedResult.getContent();

        if (beers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // Map the result with pagination info
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("currentPage", pagedResult.getNumber() + 1);
        response.put("totalPages", pagedResult.getTotalPages());
        response.put("pageSize", pageSize);
        response.put("totalItems", pagedResult.getTotalElements());
        response.put("beers", beers);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Beer findById(@PathVariable Long id) {

        Beer beer = beerSv.findById(id);

        if (beer == null) {
            throw new NotFoundException("Beer not found with id: " + id);
        }

        return beer;
    }

    @PostMapping()
    public Beer addBeer(@RequestBody Beer beer) {

        if(beer.getId() != null) {
            throw new IdSpecifiedException("Can not specify id field when perfoming a post request.");
        }

        checkIfRequiredFieldsMissing(beer);

        checkIfNameAlreadyExists(beer.getName());

        return beerSv.save(beer);
    }

    
    @PutMapping()
    public Beer updateBeer(@RequestBody Beer beer) {

        if (beer.getId() == null) {
            throw new MissingRequiredsException("The requested action could not be executed when required fields are missing.");
        }

        checkIfRequiredFieldsMissing(beer);

        Beer beerToUpdate = beerSv.findById(beer.getId());

        if (beerToUpdate == null) {
            throw new NotFoundException("Beer not found with id: " + beer.getId());
        }

        checkIfNameAlreadyExistsExcludingId(beer.getName(), beerToUpdate.getId());

        return beerSv.save(beer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse> deleteBeer(@PathVariable Long id) {

        Beer beerToDelete = beerSv.findById(id);

        if (beerToDelete == null) {
            throw new NotFoundException("Could not find beer with: " + id + " for deletion.");
        }

        beerSv.deleteById(id);

        RestResponse response = new RestResponse(200, "Beer deleted with id: " + id, DateFormatUtil.getFormattedDate(new Date()));
        
        return new ResponseEntity<RestResponse>(response, HttpStatus.OK);
    }

    public void checkIfRequiredFieldsMissing(Beer beer) {
        if (beer.getName() == null || 
            beer.getType() == null || 
            beer.getDescription() == null || 
            beer.getAbv() == null || 
            beer.getManufacturer() == null || 
            (beer.getManufacturer() != null && beer.getManufacturer().getId() == null)) {
                throw new MissingRequiredsException("The requested action could not be executed when required fields are missing.");
        }
    }

    public void checkIfNameAlreadyExists(String name) {
        if (beerSv.countAllByName(name) > 0) {
            throw new NameAlreadyExistsException("The name of the beer already exists. Please choose another name."); 
        }
    }

    public void checkIfNameAlreadyExistsExcludingId(String name, Long id) {
        Long count = beerSv.countAllByNameExcludingId(name, id);
        if (count != null && count != 0) {
            throw new NameAlreadyExistsException("The name of the beer already exists. Please choose another name."); 
        }
    }
}
