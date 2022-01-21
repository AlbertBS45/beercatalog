package com.catalog.beercatalog.rest;

import java.util.List;

import com.catalog.beercatalog.entity.Manufacturer;
import com.catalog.beercatalog.service.ManufacturerService;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ManufacturerService manufacturerSv;

    @GetMapping()
    public List<Manufacturer> findAll() {

        return manufacturerSv.findAll();
    }

    @GetMapping("/{id}")
    public Manufacturer findById(@PathVariable Long id) {

        Manufacturer manufacturer = manufacturerSv.findById(id);

        if (manufacturer == null) {
            // TODO exception handling
        }

        return manufacturer;
    }

    @PostMapping()
    public Manufacturer addManufacturer(@RequestBody Manufacturer manufacturer) {

        // In case the id its defined we set it to 0 to manually force the insert
        manufacturer.setId(Long.valueOf(0));

        return manufacturerSv.save(manufacturer);
    }

    @PutMapping()
    public Manufacturer updateManufacturer(@RequestBody Manufacturer manufacturer) {
        return manufacturerSv.save(manufacturer);
    }

    @DeleteMapping("/{id}")
    public String deleteManufacturer(@PathVariable Long id) {

        Manufacturer manufacturerToDelete = manufacturerSv.findById(id);

        if (manufacturerToDelete == null) {
            // TODO exception handling
        }

        manufacturerSv.deleteById(id);

        return "Manufacturer deleted with id: " + id;
    }
}
