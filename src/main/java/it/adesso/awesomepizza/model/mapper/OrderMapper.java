package it.adesso.awesomepizza.model.mapper;

import it.adesso.awesomepizza.model.dto.Order;
import it.adesso.awesomepizza.model.dto.OrderRequest;
import it.adesso.awesomepizza.model.dto.OrderStatus;
import it.adesso.awesomepizza.model.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", imports = OrderStatus.class)
public interface OrderMapper {
    Order entityToDto(OrderEntity entity);
    List<Order> entityToDto(List<OrderEntity> entity);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(OrderStatus.CREATED)")
    OrderEntity dtoToEntity(OrderRequest dto);
}
