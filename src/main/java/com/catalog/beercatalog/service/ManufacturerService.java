package com.catalog.beercatalog.service;

import java.util.List;
import java.util.Optional;

import com.catalog.beercatalog.entity.Manufacturer;
import com.catalog.beercatalog.repository.ManufacturerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManufacturerService {

    @Autowired
    private ManufacturerRepository manufacturerRepo;

    public List<Manufacturer> findAll() {
        return manufacturerRepo.findAll();
    }

    public Manufacturer findById(Long id) {

        Optional<Manufacturer> manufacturerOpt = manufacturerRepo.findById(id);
        
        Manufacturer manufacturer = null;

        if (manufacturerOpt.isPresent()) {
            manufacturer = manufacturerOpt.get();
        }

        return manufacturer;
    }

    public Manufacturer save(Manufacturer manufacturer) {
        Manufacturer manufacturerAddedOrUpdated = manufacturerRepo.save(manufacturer);

        if (manufacturerAddedOrUpdated == null) {
            // TODO exception handling
        }

        return manufacturerAddedOrUpdated;
    }

    public void deleteById(Long id) {
        manufacturerRepo.deleteById(id);
    }
}
