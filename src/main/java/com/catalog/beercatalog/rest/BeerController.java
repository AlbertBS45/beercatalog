package com.catalog.beercatalog.rest;

import java.util.ArrayList;
import java.util.List;

import com.catalog.beercatalog.entity.Beer;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/beers")
public class BeerController {

    @GetMapping()
    public List<Beer> findAll() {
        // TODO
        return new ArrayList<>();
    }

    @GetMapping("/{id}")
    public Beer findById(@PathVariable Long id) {
        // TODO
        return new Beer();
    }

    @PostMapping()
    public Beer addBeer(@RequestBody Beer beer) {
        // TODO
        return new Beer();
    }

    @PutMapping()
    public Beer updateBeer(@RequestBody Beer beer) {
        // TODO
        return new Beer();
    }

    @DeleteMapping("/{id}")
    public String deleteBeer(@PathVariable Long id) {
        // TODO
        return "Beer deleted with id: ";
    }
}
