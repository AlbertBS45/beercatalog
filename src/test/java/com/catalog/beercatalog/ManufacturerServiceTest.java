package com.catalog.beercatalog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.catalog.beercatalog.service.ManufacturerService;
import com.catalog.beercatalog.utils.ServiceUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@ExtendWith(MockitoExtension.class)
public class ManufacturerServiceTest {
    
    @Mock
    private ManufacturerRepository manufacturerRepo;

    @Mock
    private BeerRepository beerRepo;

    @Mock
    private ProviderRepository providerRepo;

    @Mock
    private AuthorityRepository authorityRepo;

    @Mock
    private ServiceUtil serviceUtil;

    @InjectMocks
    private ManufacturerService manufacturerSv;

    @Test 
    public void findByAllWithPaginationAndSorting_results() {

        Manufacturer man1 = new Manufacturer(1L, "name1", "nation1");
        Manufacturer man2 = new Manufacturer(2L, "name2", "nation2");
        Manufacturer man3 = new Manufacturer(3L, "name3", "nation3");

        List<Manufacturer> manufacturers = new ArrayList<>(Arrays.asList(man1,man2,man3));
        Pageable pageable = PageRequest.of(0, 10, Sort.by(new Order(Sort.Direction.ASC, "id")));
        Page<Manufacturer> pagedContentManufacturers = new PageImpl<>(manufacturers, pageable, 3);
        
        Mockito.when(serviceUtil.generatePagingAndSorting(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(String[].class))).thenReturn(pageable);
        Mockito.when(manufacturerRepo.findAllManufacturersWithPaginationAndSorting(Mockito.anyString(), Mockito.anyString(), Mockito.any(Pageable.class))).thenReturn(pagedContentManufacturers);
        Page<Manufacturer> pagedContentFinded = manufacturerSv.findAll("name", "nation", 1, 10, new String[]{});
        assertNotNull(pagedContentFinded);
        assertEquals(pagedContentFinded.getContent().size(), 3);
        assertNotNull(pagedContentFinded.getContent().get(0));
        assertEquals(pagedContentFinded.getContent().get(0).getId(), 1L);
        assertEquals(pagedContentFinded.getContent().get(0).getName(), "name1");
        assertEquals(pagedContentFinded.getContent().get(0).getNationality(), "nation1");
        assertNotNull(pagedContentFinded.getContent().get(1));
        assertEquals(pagedContentFinded.getContent().get(1).getId(), 2L);
        assertEquals(pagedContentFinded.getContent().get(1).getName(), "name2");
        assertEquals(pagedContentFinded.getContent().get(1).getNationality(), "nation2");
        verify(manufacturerRepo).findAllManufacturersWithPaginationAndSorting(Mockito.anyString(),Mockito.anyString(),Mockito.any(Pageable.class));
    }

    @Test 
    public void findByAllWithPaginationAndSorting_noResults() {

        List<Manufacturer> manufacturers = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(new Order(Sort.Direction.ASC, "id")));
        Page<Manufacturer> pagedContentManufacturers = new PageImpl<>(manufacturers, pageable, 3);
        
        Mockito.when(serviceUtil.generatePagingAndSorting(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(String[].class))).thenReturn(pageable);
        Mockito.when(manufacturerRepo.findAllManufacturersWithPaginationAndSorting(Mockito.anyString(), Mockito.anyString(), Mockito.any(Pageable.class))).thenReturn(pagedContentManufacturers);
        Page<Manufacturer> pagedContentFinded = manufacturerSv.findAll("name", "nation", 1, 10, new String[]{});
        assertNotNull(pagedContentFinded);
        assertEquals(pagedContentFinded.getContent().size(), 0);
        verify(manufacturerRepo).findAllManufacturersWithPaginationAndSorting(Mockito.anyString(),Mockito.anyString(),Mockito.any(Pageable.class));
    }

    @Test 
    public void findById_result() {

        Manufacturer manFinded = new Manufacturer(1L, "name", "nation");
        Optional<Manufacturer> optMan = Optional.of(manFinded);

        Mockito.when(manufacturerRepo.findById(Mockito.anyLong())).thenReturn(optMan);
        Manufacturer outputMan = manufacturerSv.findById(1L);
        assertNotNull(outputMan);
        assertEquals(1L, outputMan.getId());
        assertEquals("name", outputMan.getName());
        assertEquals("nation", outputMan.getNationality());
        verify(manufacturerRepo).findById(1L);
    }

    @Test 
    public void findById_noResult() {

        Optional<Manufacturer> optMan = Optional.empty();

        Mockito.when(manufacturerRepo.findById(Mockito.anyLong())).thenReturn(optMan);
        Manufacturer outputMan = manufacturerSv.findById(1L);
        assertEquals(null, outputMan);
        verify(manufacturerRepo).findById(1L);
    }

    @Test 
    public void insertManufacturerAndRelatedProviderPlusAuthority_success() {

        Manufacturer man = new Manufacturer(null, "name", "nation");
        Manufacturer manSaved = new Manufacturer(2L, "name", "nation");
        Provider provider = new Provider(1L, "name@provider.com", "$2a$12$tu/q0PZskDF9kk.KRPnys.aQcnqU6JdLPOp4bOjIgfwK07N4nSDRC", manSaved, null);
        Authority auth = new Authority(1L, "ROLE_MANUFACTURER", provider);

        Mockito.when(manufacturerRepo.save(man)).thenReturn(manSaved);
        Manufacturer outputMan = manufacturerSv.save(man);
        assertNotNull(outputMan);
        assertEquals(2L, outputMan.getId());
        verify(manufacturerRepo).save(man);

        Mockito.when(providerRepo.save(Mockito.any(Provider.class))).thenReturn(provider);
        Provider insertedProvider = providerRepo.save(provider);
        assertNotNull(insertedProvider);
        assertEquals(1L, insertedProvider.getId());
        assertEquals("name@provider.com", insertedProvider.getEmail());
        assertEquals("$2a$12$tu/q0PZskDF9kk.KRPnys.aQcnqU6JdLPOp4bOjIgfwK07N4nSDRC", insertedProvider.getPwd());
        assertNotNull(insertedProvider.getManufacturer());
        assertEquals(2L, insertedProvider.getManufacturer().getId());
        verify(providerRepo).save(provider);

        Mockito.when(authorityRepo.save(Mockito.any(Authority.class))).thenReturn(auth);
        Authority authorityRelatedToMan = authorityRepo.save(auth);
        assertNotNull(authorityRelatedToMan);
        assertEquals(1L, authorityRelatedToMan.getId());
        assertEquals("ROLE_MANUFACTURER", authorityRelatedToMan.getName());
        assertNotNull(authorityRelatedToMan.getProvider());
        assertEquals(1L, authorityRelatedToMan.getProvider().getId());
        verify(authorityRepo).save(auth);
    }

    @Test 
    public void updateManufacturer_success() {

        Manufacturer man = new Manufacturer(1L, "name", "nation");
        Manufacturer manSaved = new Manufacturer(1L, "nameUpdated", "nationUpdated");

        Mockito.when(manufacturerRepo.save(man)).thenReturn(manSaved);
        Manufacturer outputMan = manufacturerSv.save(man);
        assertNotNull(outputMan);
        assertEquals(1L, outputMan.getId());
        assertEquals("nameUpdated", outputMan.getName());
        assertEquals("nationUpdated", outputMan.getNationality());
        verify(manufacturerRepo).save(man);
    }

    @Test 
    public void deleteManufacturerByIdAndRelatedBeersAndProviderAuthorities_success() {

        Manufacturer manufacturer = new Manufacturer(1L, "name", "nationality");
        Beer beer1 = new Beer(1L, "name", "type", "desc", BigDecimal.valueOf(3.5), manufacturer);
        Beer beer2 = new Beer(2L, "name", "type", "desc", BigDecimal.valueOf(3.5), manufacturer);
        List<Beer> beersRelatedToManufacturer = new ArrayList<>(Arrays.asList(beer1,beer2));

        Mockito.when(beerRepo.findAllByManufacturer_Id(Mockito.anyLong())).thenReturn(beersRelatedToManufacturer);
        doNothing().when(beerRepo).deleteAll(beersRelatedToManufacturer);
        doNothing().when(providerRepo).deleteByManufacturer_Id(1L);
        doNothing().when(manufacturerRepo).deleteById(1L);

        beerRepo.findAllByManufacturer_Id(1L);
        verify(beerRepo).findAllByManufacturer_Id(1L);
        beerRepo.deleteAll(beersRelatedToManufacturer);
        verify(beerRepo).deleteAll(beersRelatedToManufacturer);
        providerRepo.deleteByManufacturer_Id(1L);
        verify(providerRepo).deleteByManufacturer_Id(1L);
        manufacturerSv.deleteById(1L);
        verify(manufacturerRepo).deleteById(1L);
    }

    @Test 
    public void countAllManufacturersByName_result() {

        Manufacturer man1 = new Manufacturer(1L, "name", "nation");
        Manufacturer man2 = new Manufacturer(2L, "name", "nation");
        List<Manufacturer> manufacturers = new ArrayList<>(Arrays.asList(man1,man2));

        Mockito.when(manufacturerRepo.countAllByName(Mockito.anyString())).thenReturn(Long.valueOf(manufacturers.size()));
        Long count = manufacturerSv.countAllByName("name");
        assertNotNull(count);
        assertEquals(2L, count);
        verify(manufacturerRepo).countAllByName("name");
    }

    @Test 
    public void countAllManufacturersByName_noResult() {

        Mockito.when(manufacturerRepo.countAllByName(Mockito.anyString())).thenReturn(0L);
        Long count = manufacturerSv.countAllByName("name");
        assertNotNull(count);
        assertEquals(0L, count);
        verify(manufacturerRepo).countAllByName("name");
    }

}
