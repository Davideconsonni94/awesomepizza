package it.adesso.awesomepizza.model.entity;

import it.adesso.awesomepizza.model.dto.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customer;
    private String pizza;
    private OrderStatus status;
}
