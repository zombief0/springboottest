package com.example.springtestless.web.controllers;

import com.example.springtestless.services.BeerService;
import com.example.springtestless.web.model.BeerDto;
import com.example.springtestless.web.model.BeerPagedList;
import com.example.springtestless.web.model.BeerStyleEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @MockBean
    BeerService beerService;

    @Autowired
    MockMvc mockMvc;

    BeerDto validBeer;

    BeerPagedList beerPagedList;

    @Captor
    ArgumentCaptor<String> beerNameCaptor;

    @Captor
    ArgumentCaptor<PageRequest> pageRequestCaptor;

    @Captor
    ArgumentCaptor<BeerStyleEnum> beerStyleEnumCaptor;

    @BeforeEach
    void setUp() {
        validBeer = BeerDto.builder().id(UUID.randomUUID())
                .version(1)
                .beerName("Beer1")
                .beerStyle(BeerStyleEnum.PALE_ALE)
                .price(new BigDecimal("12.99"))
                .quantityOnHand(4)
                .upc(123456789012L)
                .createdDate(OffsetDateTime.now())
                .lastModifiedDate(OffsetDateTime.now())
                .build();
        List<BeerDto> beerDtos = new ArrayList<>();
        beerDtos.add(validBeer);

        beerPagedList = new BeerPagedList(beerDtos, PageRequest.of(1,1),2L);

        given(beerService.listBeers(beerNameCaptor.capture(),beerStyleEnumCaptor.capture(),pageRequestCaptor.capture())).willReturn(beerPagedList);
    }

    @AfterEach
    void tearDown() {
        reset(beerService);
    }

    @Test
    void testGetBeerById() throws Exception {
        given(beerService.findBeerById(any())).willReturn(validBeer);

        mockMvc.perform(get("/api/v1/beer/" + validBeer.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(validBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is("Beer1")));

    }


    @Test
    void testListBeer() throws Exception {
        mockMvc.perform(get("/api/v1/beer").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(1)))
                .andExpect(jsonPath("$.content[0].id",is(validBeer.getId().toString())));
    }
}

