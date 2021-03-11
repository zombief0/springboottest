package com.example.springtestless.web.controllers;

import com.example.springtestless.domain.Customer;
import com.example.springtestless.repositories.CustomerRepository;
import com.example.springtestless.web.model.BeerOrderPagedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BeerOrderControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    Customer customer;

    @BeforeEach
    void setUp() {
        customer = customerRepository.findAll().get(0);
    }

    @Test
    void orderListTest() {
        BeerOrderPagedList beerOrderDtos = testRestTemplate.getForObject("/api/v1/customers/{customerId}/orders",BeerOrderPagedList.class,customer.getId());
        assertThat(beerOrderDtos.getSize()).isGreaterThan(0);
        System.out.println("Order Tests");
    }
}
