package it.adesso.awesomepizza.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Order {
    private Long id;
    private String customer;
    private String pizza;
    private OrderStatus status;
}
