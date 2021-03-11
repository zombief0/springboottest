package com.example.springtestless.web.controllers;

import com.example.springtestless.services.BeerOrderService;
import com.example.springtestless.web.model.BeerOrderDto;
import com.example.springtestless.web.model.BeerOrderPagedList;
import com.example.springtestless.web.model.OrderStatusEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerOrderController.class)
class BeerOrderControllerTest {
    @MockBean
    BeerOrderService beerOrderService;

    @Autowired
    MockMvc mockMvc;

    BeerOrderDto beerOrder1;
    BeerOrderDto beerOrder2;

    @Captor
    ArgumentCaptor<PageRequest> pageRequestCaptor;

    @Captor
    ArgumentCaptor<UUID> idCaptor;

    BeerOrderPagedList beerOrderPagedList;

    @BeforeEach
    void setUp() {
        beerOrder1 = BeerOrderDto.builder()
                .version(1)
                .id(UUID.randomUUID())
                .customerRef("sfr5522")
                .customerId(UUID.randomUUID())
                .orderStatus(OrderStatusEnum.NEW)
                .orderStatusCallbackUrl("srrefref")
                .createdDate(OffsetDateTime.now())
                .lastModifiedDate(OffsetDateTime.now())
                .build();

        beerOrder2 = BeerOrderDto.builder()
                .version(1)
                .id(UUID.randomUUID())
                .customerRef("kfhukrure")
                .customerId(UUID.randomUUID())
                .orderStatus(OrderStatusEnum.NEW)
                .orderStatusCallbackUrl("kefnwanef")
                .createdDate(OffsetDateTime.now())
                .lastModifiedDate(OffsetDateTime.now())
                .build();
    }

    @AfterEach
    void tearDown() {
        reset(beerOrderService);
    }

    @Test
    void orderList() throws Exception {


        List<BeerOrderDto> beerOrderDtos = new ArrayList<>(Arrays.asList(beerOrder1, beerOrder2));

        beerOrderPagedList = new BeerOrderPagedList(beerOrderDtos,PageRequest.of(1,2),1L);
        given(beerOrderService.listOrders(any(),any())).willReturn(beerOrderPagedList);

        mockMvc.perform(get("/api/v1/customers/{customerId}/orders",beerOrder1.getCustomerId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(2)));

    }
}
