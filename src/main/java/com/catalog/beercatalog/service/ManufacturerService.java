package com.catalog.beercatalog.service;

import java.util.List;
import java.util.Optional;

import com.catalog.beercatalog.entity.Authority;
import com.catalog.beercatalog.entity.Beer;
import com.catalog.beercatalog.entity.Manufacturer;
import com.catalog.beercatalog.entity.Provider;
import com.catalog.beercatalog.repository.AuthorityRepository;
import com.catalog.beercatalog.repository.BeerRepository;
import com.catalog.beercatalog.repository.ManufacturerRepository;
import com.catalog.beercatalog.repository.ProviderRepository;
import com.catalog.beercatalog.utils.ServiceUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ManufacturerService {

    @Autowired
    private ManufacturerRepository manufacturerRepo;

    @Autowired
    private BeerRepository beerRepo;

    @Autowired
    private ProviderRepository providerRepo;

    @Autowired
    private AuthorityRepository authorityRepo;

    @Autowired
    private ServiceUtil serviceUtil;

    @Transactional(readOnly = true)
    public Page<Manufacturer> findAll(
        String name,
        String nationality,
        Integer pageNum, 
        Integer pageSize, 
        String[] sort) 
    {
        // Setting the correct format for pageNum
        pageNum--;

        // Making the request pageable and sortable
        Pageable paging = serviceUtil.generatePagingAndSorting(pageNum, pageSize, sort);
        
        // Setting params values for query
        String ManufacturerParamName = (StringUtils.hasText(name) ? name : null);
        String ManufacturerParamNat = (StringUtils.hasText(nationality) ? nationality : null);

        // Executing query
        Page<Manufacturer> pagedResult = manufacturerRepo.findAllManufacturersWithPaginationAndSorting(
            ManufacturerParamName,
            ManufacturerParamNat,
            paging);

        return pagedResult; 
    }

    @Transactional(readOnly = true)
    public Manufacturer findById(Long id) {

        Optional<Manufacturer> manufacturerOpt = manufacturerRepo.findById(id);
        
        Manufacturer manufacturer = null;

        if (manufacturerOpt.isPresent()) {
            manufacturer = manufacturerOpt.get();
        }

        return manufacturer;
    }

    @Transactional
    @PreAuthorize("@manufacturerCheckMethodSecurity.isItOwnManufacturer(#manufacturer)")
    public Manufacturer save(Manufacturer manufacturer) {

        Boolean hasId = manufacturer.getId() != null ? true : false;

        Manufacturer manufacturerAddedOrUpdated = manufacturerRepo.save(manufacturer);

        // It was an insertion
        if (!hasId) {

            // Creating user/provider related to manufacturer
            String normalizedUsername = manufacturer.getName().replaceAll("\\s+","").trim().toLowerCase().concat("@provider.com");

            Provider provider = new Provider(
                normalizedUsername, "$2a$12$tu/q0PZskDF9kk.KRPnys.aQcnqU6JdLPOp4bOjIgfwK07N4nSDRC", manufacturerAddedOrUpdated, null);

            Provider insertedProvider = providerRepo.save(provider);

            // Insert related authority to new created provider
            Authority authority = new Authority("ROLE_MANUFACTURER", insertedProvider);
            authorityRepo.save(authority);
        }

        return manufacturerAddedOrUpdated;
    }

    @Transactional
    public void deleteById(Long id) {

        //Deleting all beers related to the manufacturer
        List<Beer> beersRelatedToManufacturer = beerRepo.findAllByManufacturer_Id(id);
        beerRepo.deleteAll(beersRelatedToManufacturer);

        //Deleting user/provider and authorities related to the manufacturer
        providerRepo.deleteByManufacturer_Id(id);

        //Deleting manufacturer
        manufacturerRepo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Long countAllByName(String name) {
        return manufacturerRepo.countAllByName(name);
    }

    @Transactional(readOnly = true)
    public Long countAllByNameExcludingId(String name, Long id) {
        return manufacturerRepo.countByNameExcludingId(name, id);
    }
    
}
