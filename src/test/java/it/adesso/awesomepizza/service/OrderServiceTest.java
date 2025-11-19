package it.adesso.awesomepizza.service;

import it.adesso.awesomepizza.exeption.ResourceConflictException;
import it.adesso.awesomepizza.exeption.ResourceNotFoundException;
import it.adesso.awesomepizza.model.dto.Event;
import it.adesso.awesomepizza.model.dto.OrderRequest;
import it.adesso.awesomepizza.model.dto.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired OrderService orderService;

    final OrderRequest TEST_ORDER = new OrderRequest("davide","margherita");

    @Test
    void getOrderById() {
        var id = orderService.createOrder(TEST_ORDER);
        var result = orderService.getOrderById(id);
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(TEST_ORDER.getCustomer(), result.getCustomer());
        assertEquals(TEST_ORDER.getPizza(), result.getPizza());
        assertEquals(OrderStatus.CREATED, result.getStatus());
    }

    @Test
    void getOrderById_NotFound() {
        assertThrows(ResourceNotFoundException.class, ()->orderService.getOrderById(100L));
    }

    @Test
    void getOrders_Empty() {
        var emptyResult= orderService.getOrders(null);
        assertNotNull(emptyResult);
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    void getOrders_NotEmpty() {
        var id = orderService.createOrder(TEST_ORDER);
        var notEmptyResult = orderService.getOrders(null);
        assertNotNull(notEmptyResult);
        assertFalse(notEmptyResult.isEmpty());
        assertEquals(1, notEmptyResult.size());
        assertTrue(notEmptyResult.stream().anyMatch(r->id.equals(r.getId())));
    }

    @Test
    void getOrders_FilteredWithResult() {
        var id = orderService.createOrder(TEST_ORDER);
        var notEmptyResult = orderService.getOrders(OrderStatus.CREATED);
        assertNotNull(notEmptyResult);
        assertFalse(notEmptyResult.isEmpty());
        assertEquals(1, notEmptyResult.size());
        assertTrue(notEmptyResult.stream().anyMatch(r->id.equals(r.getId())));
    }

    @Test
    void getOrders_FilteredEmpty() {
        orderService.createOrder(TEST_ORDER);
        var emptyResult = orderService.getOrders(OrderStatus.WORKING);
        assertNotNull(emptyResult);
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    void updateOrderStatus() {
        var id = orderService.createOrder(TEST_ORDER);
        var result1 = orderService.updateOrderStatus(id, Event.BEGIN_PROCESSING);
        assertNotNull(result1);
        assertEquals(OrderStatus.WORKING, result1);
        var result2 = orderService.updateOrderStatus(id, Event.FINISH_PROCESSING);
        assertNotNull(result2);
        assertEquals(OrderStatus.PROCESSED, result2);

    }

    @Test
    void updateOrderStatus_FailedForWrongStatus() {
        var id = orderService.createOrder(TEST_ORDER);
        assertThrows(ResourceConflictException.class, ()-> orderService.updateOrderStatus(id, Event.FINISH_PROCESSING));
    }

    @Test
    void updateOrderStatus_FailedForExistingWorkingOrder() {
        var id = orderService.createOrder(TEST_ORDER);
        orderService.updateOrderStatus(id, Event.BEGIN_PROCESSING);
        var id2 = orderService.createOrder(TEST_ORDER);
        assertThrows(ResourceConflictException.class, ()-> orderService.updateOrderStatus(id2, Event.BEGIN_PROCESSING));
    }
}