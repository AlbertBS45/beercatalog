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

import com.catalog.beercatalog.entity.Beer;
import com.catalog.beercatalog.entity.Manufacturer;
import com.catalog.beercatalog.repository.BeerRepository;
import com.catalog.beercatalog.service.BeerService;
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
import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTest {
    
    @Mock
    private BeerRepository beerRepo;

    @Mock
    private ServiceUtil serviceUtil;

    @InjectMocks
    private BeerService beerSv;

    @Test 
    public void findByAllWithPaginationAndSorting_results() {

        Manufacturer man1 = new Manufacturer(1L, "manufacturer1", "nation1");
        Manufacturer man2 = new Manufacturer(2L, "manufacturer2", "nation2");

        Beer beer1 = new Beer(1L, "beer1", "type1", "description1", BigDecimal.valueOf(4.5), man1);
        Beer beer2 = new Beer(1L, "beer2", "type2", "description2", BigDecimal.valueOf(5.5), man1);
        Beer beer3 = new Beer(1L, "beer3", "type3", "description3", BigDecimal.valueOf(6.5), man2);

        List<Beer> beers = new ArrayList<>(Arrays.asList(beer1,beer2,beer3));
        Pageable pageable = PageRequest.of(0, 10, Sort.by(new Order(Sort.Direction.ASC, "id")));
        Page<Beer> pagedContentBeers = new PageImpl<>(beers, pageable, 3);
        
        Mockito
            .when(serviceUtil.generatePagingAndSorting(
                Mockito.anyInt(), 
                Mockito.anyInt(), 
                Mockito.any(String[].class)))
            .thenReturn(pageable);
        Mockito
            .when(beerRepo.findAllBeersWithPaginationAndSorting(
                Mockito.anyString(), 
                Mockito.anyString(), 
                Mockito.anyString(), 
                Mockito.any(BigDecimal.class), 
                Mockito.any(BigDecimal.class), 
                Mockito.anyLong(), 
                Mockito.anyString(), 
                Mockito.anyString(), 
                Mockito.any(Pageable.class)))
            .thenReturn(pagedContentBeers);

        Page<Beer> pagedContentFinded = beerSv.findAll(
            "name", 
            "type", 
            "desc", 
            BigDecimal.valueOf(2.0), 
            BigDecimal.valueOf(2.0), 
            1L,
            "manname",
            "mannation",
            1,
            10,
            new String[]{});
        
        assertNotNull(pagedContentFinded);
        assertEquals(pagedContentFinded.getContent().size(), 3);
        assertNotNull(pagedContentFinded.getContent().get(0));
        assertEquals(pagedContentFinded.getContent().get(0).getId(), 1L);
        assertEquals(pagedContentFinded.getContent().get(0).getName(), "beer1");
        assertEquals(pagedContentFinded.getContent().get(0).getType(), "type1");
        assertEquals(pagedContentFinded.getContent().get(0).getDescription(), "description1");
        assertEquals(pagedContentFinded.getContent().get(0).getAbv(), BigDecimal.valueOf(4.5));
        assertNotNull(pagedContentFinded.getContent().get(0).getManufacturer());
        assertEquals(pagedContentFinded.getContent().get(0).getManufacturer().getId(), 1L);
        assertEquals(pagedContentFinded.getContent().get(0).getManufacturer().getName(), "manufacturer1");
        assertEquals(pagedContentFinded.getContent().get(0).getManufacturer().getNationality(), "nation1");
        assertNotNull(pagedContentFinded.getContent().get(2));
        assertEquals(pagedContentFinded.getContent().get(2).getId(), 1L);
        assertEquals(pagedContentFinded.getContent().get(2).getName(), "beer3");
        assertEquals(pagedContentFinded.getContent().get(2).getType(), "type3");
        assertEquals(pagedContentFinded.getContent().get(2).getDescription(), "description3");
        assertEquals(pagedContentFinded.getContent().get(2).getAbv(), BigDecimal.valueOf(6.5));
        assertNotNull(pagedContentFinded.getContent().get(2).getManufacturer());
        assertEquals(pagedContentFinded.getContent().get(2).getManufacturer().getId(), 2L);
        assertEquals(pagedContentFinded.getContent().get(2).getManufacturer().getName(), "manufacturer2");
        assertEquals(pagedContentFinded.getContent().get(2).getManufacturer().getNationality(), "nation2");
        verify(beerRepo).findAllBeersWithPaginationAndSorting(
            Mockito.anyString(), 
            Mockito.anyString(), 
            Mockito.anyString(), 
            Mockito.any(BigDecimal.class), 
            Mockito.any(BigDecimal.class), 
            Mockito.anyLong(), 
            Mockito.anyString(), 
            Mockito.anyString(), 
            Mockito.any(Pageable.class));
    }

    @Test 
    public void findByAllWithPaginationAndSorting_noResults() {

        List<Beer> beers = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(new Order(Sort.Direction.ASC, "id")));
        Page<Beer> pagedContentBeers = new PageImpl<>(beers, pageable, 3);
        
        Mockito
            .when(serviceUtil.generatePagingAndSorting(
                Mockito.anyInt(), 
                Mockito.anyInt(), 
                Mockito.any(String[].class)))
            .thenReturn(pageable);
        Mockito
            .when(beerRepo.findAllBeersWithPaginationAndSorting(
                Mockito.anyString(), 
                Mockito.anyString(), 
                Mockito.anyString(), 
                Mockito.any(BigDecimal.class), 
                Mockito.any(BigDecimal.class), 
                Mockito.anyLong(), 
                Mockito.anyString(), 
                Mockito.anyString(), 
                Mockito.any(Pageable.class)))
            .thenReturn(pagedContentBeers);

        Page<Beer> pagedContentFinded = beerSv.findAll(
            "name", 
            "type", 
            "desc", 
            BigDecimal.valueOf(2.0), 
            BigDecimal.valueOf(2.0), 
            1L,
            "manname",
            "mannation",
            1,
            10,
            new String[]{});
        
        assertNotNull(pagedContentFinded);
        assertEquals(pagedContentFinded.getContent().size(), 0);
        verify(beerRepo).findAllBeersWithPaginationAndSorting(
            Mockito.anyString(), 
            Mockito.anyString(), 
            Mockito.anyString(), 
            Mockito.any(BigDecimal.class), 
            Mockito.any(BigDecimal.class), 
            Mockito.anyLong(), 
            Mockito.anyString(), 
            Mockito.anyString(), 
            Mockito.any(Pageable.class));
    }

    @Test 
    public void findById_result() {

        Manufacturer man = new Manufacturer(1L, "name", "nation");
        Beer beerFinded = new Beer(1L, "name", "type", "desc", BigDecimal.valueOf(4.0), man);
        Optional<Beer> optBeer = Optional.of(beerFinded);

        Mockito.when(beerRepo.findById(Mockito.anyLong())).thenReturn(optBeer);
        Beer outputBeer = beerSv.findById(1L);
        assertNotNull(outputBeer);
        assertEquals(1L, outputBeer.getId());
        assertEquals("name", outputBeer.getName());
        assertEquals("type", outputBeer.getType());
        assertEquals("desc", outputBeer.getDescription());
        assertEquals(BigDecimal.valueOf(4.0), outputBeer.getAbv());
        assertNotNull(outputBeer.getManufacturer());
        assertEquals(1L, outputBeer.getManufacturer().getId());
        assertEquals("name", outputBeer.getManufacturer().getName());
        assertEquals("nation", outputBeer.getManufacturer().getNationality());
        verify(beerRepo).findById(1L);
    }

    @Test 
    public void findById_noResult() {

        Optional<Beer> optBeer = Optional.empty();

        Mockito.when(beerRepo.findById(Mockito.anyLong())).thenReturn(optBeer);
        Beer outputBeer = beerSv.findById(1L);
        assertEquals(null, outputBeer);
        verify(beerRepo).findById(1L);
    }

    @Test
    @WithMockUser(roles = {"ADMIN","MANUFACTURER"})
    public void saveBeer_success() {

        Beer beer = new Beer();
        Manufacturer man = new Manufacturer(1L, null, null);
        Beer beerSaved = new Beer(1L, "name", "type", "desc", BigDecimal.valueOf(4.0), man);

        Mockito.when(beerRepo.save(beer)).thenReturn(beerSaved);
        Beer outputBeer = beerSv.save(beer);
        assertNotNull(outputBeer);
        assertEquals(1L, outputBeer.getId());
        assertEquals("name", outputBeer.getName());
        assertEquals("type", outputBeer.getType());
        assertEquals("desc", outputBeer.getDescription());
        assertEquals(BigDecimal.valueOf(4.0), outputBeer.getAbv());
        assertNotNull(outputBeer.getManufacturer());
        assertEquals(1L, outputBeer.getManufacturer().getId());
        verify(beerRepo).save(beer);
    }

    @Test 
    public void deleteBeerById_success() {

        doNothing().when(beerRepo).deleteById(1L);
        beerSv.deleteById(1L);
        verify(beerRepo).deleteById(1L);
    }

    @Test 
    public void countAllBeersByName_result() {

        Manufacturer man = new Manufacturer(1L, "name", "nation");
        Beer beer1 = new Beer(1L, "name", "type", "desc", BigDecimal.valueOf(4.0), man);
        Beer beer2 = new Beer(2L, "name", "type", "desc", BigDecimal.valueOf(2.0), man);
        List<Beer> beers = new ArrayList<>(Arrays.asList(beer1,beer2));

        Mockito.when(beerRepo.countAllByName(Mockito.anyString())).thenReturn(Long.valueOf(beers.size()));
        Long count = beerSv.countAllByName("name");
        assertNotNull(count);
        assertEquals(2L, count);
        verify(beerRepo).countAllByName("name");
    }

    @Test 
    public void countAllBeersByName_noResult() {

        Mockito.when(beerRepo.countAllByName(Mockito.anyString())).thenReturn(0L);
        Long count = beerSv.countAllByName("name");
        assertNotNull(count);
        assertEquals(0L, count);
        verify(beerRepo).countAllByName("name");
    }

}
