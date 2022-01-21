package com.catalog.beercatalog.service;

import java.util.List;
import java.util.Optional;

import com.catalog.beercatalog.entity.Manufacturer;
import com.catalog.beercatalog.repository.ManufacturerRepository;
import com.catalog.beercatalog.service.utils.ServiceUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ManufacturerService {

    @Autowired
    private ManufacturerRepository manufacturerRepo;

    public List<Manufacturer> findAll(
        String name,
        String nationality,
        Integer pageNum, 
        Integer pageSize, 
        String[] sort) 
    {
        // Making the request pageable and sortable
        Pageable paging = ServiceUtils.generatePagingAndSorting(pageNum, pageSize, sort);
        
        // Setting params values for query
        String ManufacturerParamName = (StringUtils.hasText(name) ? name : null);
        String ManufacturerParamNat = (StringUtils.hasText(nationality) ? nationality : null);

        // Executing query
        Page<Manufacturer> pagedResult = manufacturerRepo.findAllManufacturersWithPaginationAndSorting(
            ManufacturerParamName,
            ManufacturerParamNat,
            paging);

        return pagedResult.getContent(); 
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
