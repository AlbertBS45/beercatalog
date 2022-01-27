package com.catalog.beercatalog.security;

import java.util.Optional;

import com.catalog.beercatalog.entity.Beer;
import com.catalog.beercatalog.entity.Manufacturer;
import com.catalog.beercatalog.entity.Provider;
import com.catalog.beercatalog.exception.NotFoundException;
import com.catalog.beercatalog.repository.BeerRepository;
import com.catalog.beercatalog.repository.ManufacturerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ManufacturerCheckMethodSecurity {
    
    @Autowired
    private BeerRepository beerRepo;

    @Autowired
    private ManufacturerRepository manufacturerRepo;

    public boolean isItOwnManufacturer(Object object) throws NotFoundException {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_ADMIN"));
        Boolean isManufacturer = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_MANUFACTURER"));
        
        // Verify if auth user has the role Admin
        if (isAdmin) {
            return true;
        }
        
        // Verify if authorized user has role Manufacturer
        if (isManufacturer) {

            Long manufacturerId = null;
            // Coming from BeerController POST or PUT
            if (object instanceof Beer) {
                Beer beer = (Beer) object;

                // Its an update 
                if(beer.getId() != null) {

                    //Retrieve beer by Id 
                    Optional<Beer> beerToUpdate = beerRepo.findById(beer.getId());
                    if (beerToUpdate.isPresent()) {
                        manufacturerId = beerToUpdate.get().getManufacturer().getId();
                    } else {
                        throw new NotFoundException("Beer not found with id: " + (Long) object);
                    }
                } else {
                    // Its an insert retrieve manufacturer id from beer json input
                    manufacturerId = beer.getManufacturer().getId();
                }

            // Coming from ManufacturerController PUT
            } else if (object instanceof Manufacturer) {
                Manufacturer manufacturer = (Manufacturer) object;

                Optional<Manufacturer> manToUpdate = manufacturerRepo.findById(manufacturer.getId());
                if (manToUpdate.isPresent()) {
                    manufacturerId = manToUpdate.get().getId();
                } else {
                    throw new NotFoundException("Manufacturer not found with id: " + manufacturer.getId());
                }
            } else {
                // Coming from BeerController DELETE
                Optional<Beer> beerToDelete = beerRepo.findById((Long) object);
                if (beerToDelete.isPresent()) {
                    manufacturerId = beerToDelete.get().getManufacturer().getId();
                } else {
                    throw new NotFoundException("Beer not found with id: " + (Long) object);
                }
            }

            // Obtain manufacturer id from authenticated user
            Provider authenticatedProvider = (Provider) authentication.getPrincipal();
            Long authUserManufacturerId = authenticatedProvider.getManufacturer().getId();

            //Proceed to check if is it own beer manufacturer
            if (manufacturerId != null && manufacturerId == authUserManufacturerId) {
                return true;
            }
        }
        
        return false;
    }
}
