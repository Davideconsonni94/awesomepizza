package it.adesso.awesomepizza.controller;

import it.adesso.awesomepizza.model.dto.Event;
import it.adesso.awesomepizza.model.dto.Order;
import it.adesso.awesomepizza.model.dto.OrderRequest;
import it.adesso.awesomepizza.model.dto.OrderStatus;
import it.adesso.awesomepizza.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;
    @PostMapping()
    public ResponseEntity<Long> createOrder(@Valid @RequestBody OrderRequest request){
        final var orderId = orderService.createOrder(request);
        return ResponseEntity.ok(orderId);
    }
    @GetMapping()
    public ResponseEntity<List<Order>> getOrders(@RequestParam(value = "status",required = false) OrderStatus statusFilter){
        final var orderList = orderService.getOrders(statusFilter);
        return ResponseEntity.ok(orderList);
    }

    @GetMapping(value = "/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable("orderId") Long orderId){
        final var order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
    @PatchMapping(value = "/{orderId}/event/{event}")
    public ResponseEntity<OrderStatus> updateOrderStatus(@PathVariable("orderId") Long orderId, @PathVariable("event") Event event){
        final var orderStatus = orderService.updateOrderStatus(orderId, event);
        return ResponseEntity.ok(orderStatus);
    }
}
