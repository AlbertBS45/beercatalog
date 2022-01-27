package com.catalog.beercatalog.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.catalog.beercatalog.entity.Manufacturer;
import com.catalog.beercatalog.exception.IdSpecifiedException;
import com.catalog.beercatalog.exception.MissingRequiredsException;
import com.catalog.beercatalog.exception.NameAlreadyExistsException;
import com.catalog.beercatalog.exception.NotFoundException;
import com.catalog.beercatalog.service.ManufacturerService;
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
@RequestMapping("/manufacturers")
public class ManufacturerController {
    
    @Autowired
    private ManufacturerService manufacturerSv;

    @GetMapping()
    public ResponseEntity<Map<String, Object>> findAll(
        @RequestParam(required = false, name = "name") String name,
        @RequestParam(required = false, name = "nationality") String nationality,
        @RequestParam(required = false, defaultValue = "1", name = "pageNum") Integer pageNum,
        @RequestParam(required = false, defaultValue = "10", name = "pageSize") Integer pageSize,
        @RequestParam(required = false, defaultValue = "id,asc", name = "sort") String[] sort) 
    {

        List<Manufacturer> manufacturers = new ArrayList<Manufacturer>();

        // Get the paged and sorted result
        Page<Manufacturer> pagedResult = manufacturerSv.findAll(
            name,
            nationality,
            pageNum, 
            pageSize, 
            sort);

        manufacturers = pagedResult.getContent();

        if (manufacturers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // Map the result with pagination info
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("currentPage", pagedResult.getNumber() + 1);
        response.put("totalPages", pagedResult.getTotalPages());
        response.put("pageSize", pageSize);
        response.put("totalItems", pagedResult.getTotalElements());
        response.put("manufacturers", manufacturers);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Manufacturer findById(@PathVariable Long id) {

        Manufacturer manufacturer = manufacturerSv.findById(id);

        if (manufacturer == null) {
            throw new NotFoundException("Manufacturer not found with id: " + id);
        }

        return manufacturer;
    }

    @PostMapping()
    public Manufacturer addManufacturer(@RequestBody Manufacturer manufacturer) {

        if(manufacturer.getId() != null) {
            throw new IdSpecifiedException("Can not specify id field when perfoming a post request.");
        }

        checkIfRequiredFieldsMissing(manufacturer);

        checkIfNameAlreadyExists(manufacturer.getName());

        return manufacturerSv.save(manufacturer);
    }

    @PutMapping()
    public Manufacturer updateManufacturer(@RequestBody Manufacturer manufacturer) {

        if (manufacturer.getId() == null ) {
                throw new MissingRequiredsException("The requested action could not be executed when required fields are missing."); 
        }

        checkIfRequiredFieldsMissing(manufacturer);

        Manufacturer manufacturerToUpdate = manufacturerSv.findById(manufacturer.getId());

        if (manufacturerToUpdate == null) {
            throw new NotFoundException("Could not find manufacturer with: " + manufacturer.getId() + " for deletion.");
        }

        checkIfNameAlreadyExistsExcludingId(manufacturer.getName(), manufacturerToUpdate.getId());

        return manufacturerSv.save(manufacturer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse> deleteManufacturer(@PathVariable Long id) {

        Manufacturer manufacturerToDelete = manufacturerSv.findById(id);

        if (manufacturerToDelete == null) {
            throw new NotFoundException("Could not find manufacturer with: " + id + " for deletion.");
        }

        manufacturerSv.deleteById(id);

        RestResponse response = new RestResponse(
            200,
            "Manufacturer deleted with id: " + id + ". If the manufacturer had a user, authorities or related beers, they were also deleted.", 
            DateFormatUtil.getFormattedDate(new Date()));

        return new ResponseEntity<RestResponse>(response, HttpStatus.OK);
    }

    public void checkIfRequiredFieldsMissing(Manufacturer manufacturer) {
        if (manufacturer.getName() == null || manufacturer.getNationality() == null ) {
                throw new MissingRequiredsException("The requested action could not be executed when required fields are missing.");
        }
    }

    public void checkIfNameAlreadyExists(String name) {
        if (manufacturerSv.countAllByName(name) > 0) {
            throw new NameAlreadyExistsException("The name of the manufacturer already exists. Please choose another name."); 
        }
    }

    public void checkIfNameAlreadyExistsExcludingId(String name, Long id) {
        Long count = manufacturerSv.countAllByNameExcludingId(name, id);
        if (count != null && count != 0) {
            throw new NameAlreadyExistsException("The name of the manufacturer already exists. Please choose another name."); 
        }
    }
}
