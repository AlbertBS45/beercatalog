package com.catalog.beercatalog.rest;

import java.util.ArrayList;
import java.util.List;

import com.catalog.beercatalog.entity.Manufacturer;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manufacturers")
public class ManufacturerController {

    @GetMapping()
    public List<Manufacturer> findAll() {
        //TODO
        return new ArrayList<>();
    }

    @GetMapping("/{id}")
    public Manufacturer findById(@PathVariable Long id) {
        //TODO
        return new Manufacturer();
    }

    @PostMapping()
    public Manufacturer addManufacturer(@RequestBody Manufacturer manufacturer) {
        //TODO
        return new Manufacturer();
    }

    @PutMapping()
    public Manufacturer updateManufacturer(@RequestBody Manufacturer manufacturer) {
        //TODO
        return new Manufacturer();
    }

    @DeleteMapping("/{id}")
    public String deleteManufacturer(@PathVariable Long id) {   
        //TODO
        return "Manufacturer deleted with id: " + id;
    }
}

