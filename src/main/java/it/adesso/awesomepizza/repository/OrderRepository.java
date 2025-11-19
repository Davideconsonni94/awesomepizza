package it.adesso.awesomepizza.repository;

import it.adesso.awesomepizza.model.dto.OrderStatus;
import it.adesso.awesomepizza.model.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findAllByStatus(OrderStatus status);
}
