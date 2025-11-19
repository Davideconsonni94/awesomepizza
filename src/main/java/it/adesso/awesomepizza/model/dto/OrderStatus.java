package it.adesso.awesomepizza.model.dto;

import it.adesso.awesomepizza.exeption.ResourceConflictException;

import java.util.EnumMap;
import java.util.Map;

public enum OrderStatus {
    CREATED,WORKING,PROCESSED;

    private Map<OrderStatus, Map<Event, OrderStatus>> getTransitionMap() {
        Map<OrderStatus, Map<Event, OrderStatus>> transitionsMap = new EnumMap<>(OrderStatus.class);
        transitionsMap.put(OrderStatus.CREATED, new EnumMap<>(Event.class));
        transitionsMap.get(OrderStatus.CREATED).put(Event.BEGIN_PROCESSING, OrderStatus.WORKING);

        transitionsMap.put(OrderStatus.WORKING, new EnumMap<>(Event.class));
        transitionsMap.get(OrderStatus.WORKING).put(Event.FINISH_PROCESSING, OrderStatus.PROCESSED);
        return transitionsMap;
    }

    public OrderStatus nextStatus(Event event) {
        Map<Event, OrderStatus> transitionMap = getTransitionMap().get(this);
        if (transitionMap.containsKey(event)) {
            return transitionMap.get(event);
        } else {
            throw new ResourceConflictException("Invalid event: " + event + " for current state: " + this);
        }
    }
}
