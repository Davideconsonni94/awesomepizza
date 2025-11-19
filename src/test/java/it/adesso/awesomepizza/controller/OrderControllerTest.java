package it.adesso.awesomepizza.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.adesso.awesomepizza.model.dto.Event;
import it.adesso.awesomepizza.model.dto.Order;
import it.adesso.awesomepizza.model.dto.OrderRequest;
import it.adesso.awesomepizza.model.dto.OrderStatus;
import it.adesso.awesomepizza.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest {

    @Mock
    private OrderService orderService;
    @Mock
    private MockMvc mockMvc;

    private static final String API_PATH = "/api/v1/order";

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        this.mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(orderService))
                .setCustomArgumentResolvers(resolver).build();
    }


    @Test
    void createOrder() throws Exception {
        final var request = new OrderRequest("davide", "margherita");
        when(orderService.createOrder(any())).thenReturn(1L);
        mockMvc.perform(post(API_PATH)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getOrders() throws Exception {
        final var order = new Order(1L, "davide", "margherita", OrderStatus.CREATED);
        when(orderService.getOrders(OrderStatus.CREATED)).thenReturn(Collections.singletonList(order));
        mockMvc.perform(get(API_PATH)
                        .param("status", "CREATED")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getOrderById() throws Exception {
        final var order = new Order(1L, "davide", "margherita", OrderStatus.CREATED);
        when(orderService.getOrderById(1L)).thenReturn(order);
        mockMvc.perform(get(API_PATH+"/{orderId}", order.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void updateOrderStatus() throws Exception {
        when(orderService.updateOrderStatus(1L, Event.BEGIN_PROCESSING)).thenReturn(OrderStatus.CREATED);
        mockMvc.perform(patch(API_PATH+"/{orderId}/event/{event}", 1L, Event.BEGIN_PROCESSING)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}