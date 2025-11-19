package it.adesso.awesomepizza.service;

import it.adesso.awesomepizza.exeption.ResourceConflictException;
import it.adesso.awesomepizza.exeption.ResourceNotFoundException;
import it.adesso.awesomepizza.model.dto.*;
import it.adesso.awesomepizza.model.mapper.OrderMapper;
import it.adesso.awesomepizza.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public Long createOrder(OrderRequest request) {
        final var orderEntity = orderMapper.dtoToEntity(request);
        final var newOrder = orderRepository.save(orderEntity);
        return newOrder.getId();
    }

    public List<Order> getOrders(OrderStatus status) {
        final var orders = status!=null? orderRepository.findAllByStatus(status):orderRepository.findAll();
        return orderMapper.entityToDto(orders);
    }

    public Order getOrderById(Long orderId) {
        final var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));
        return orderMapper.entityToDto(order);
    }

    public OrderStatus updateOrderStatus(Long orderId, Event event) {
        final var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));
        final var nextStatus = order.getStatus().nextStatus(event);
        if(nextStatus==OrderStatus.WORKING) checkOrdersInWorkingStatus();
        order.setStatus(nextStatus);
        orderRepository.save(order);
        return nextStatus;
    }

    private void checkOrdersInWorkingStatus() {
        if (!orderRepository.findAllByStatus(OrderStatus.WORKING).isEmpty()) {
            throw new ResourceConflictException("Order in status WORKING already exists");
        }
    }
}
