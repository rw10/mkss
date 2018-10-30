package org.se.lab;

import java.util.List;

/**
 * The OrderServiceDecorator is an abstract class used for adding
 * pre-/post-processing to the OrderService
 *
 * @author rene
 */
abstract class OrderServiceDecorator implements OrderService {

    /*
     * Reference: ---[1]-> OrderService
     */
    private final OrderService service;

    /*
     * Constructor
     */
    public OrderServiceDecorator(OrderService service)
    {
        if(service == null)
            throw new IllegalArgumentException("Invalid OrderService reference!");
        this.service = service;
    }

    /*
     * Interface Operations
     */
    @Override
    public long placeOrder(Order order) {
        return service.placeOrder(order);
    }
}
