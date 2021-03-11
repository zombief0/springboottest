package com.example.springtestless.web.controllers;

import com.example.springtestless.web.model.BeerPagedList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BeerControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testListBeers(){
        BeerPagedList beerPagedList = testRestTemplate.getForObject("/api/v1/beer",BeerPagedList.class);

        assertThat(beerPagedList).hasSize(3);

        System.out.println("Beer Test");
    }
}
