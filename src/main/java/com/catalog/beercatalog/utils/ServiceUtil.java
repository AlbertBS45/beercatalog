package com.catalog.beercatalog.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

@Component
public class ServiceUtil {
    
    public Pageable generatePagingAndSorting(Integer pageNum, Integer pageSize, String[] sort) {

        List<Order> orders = new ArrayList<Order>();
        
        // Managing multiple sorting params and creating corresponding Order objects
        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
              String[] sortArray = sortOrder.split(",");
              orders.add(new Order(sortArray[1].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortArray[0]));
            }
        } else {
            orders.add(new Order(sort[1].equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sort[0]));
        }

        // Managing paging and sorting with Order objects previously created
        return PageRequest.of(pageNum, pageSize, Sort.by(orders));
    }
}

