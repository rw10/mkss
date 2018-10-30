package org.se.lab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The OrderBuilder can be used to easily create Order-Objects
 *
 * @author rene
 */
class OrderBuilder {

    private int id = -1;
    List<OrderLine> orderLines = new ArrayList<>();

    /*
     * Operations
     */
    OrderBuilder id(int id){
        // validity check
        if (id < 0){
            throw new IllegalArgumentException("Negative ID is not allowed!");
        }

        this.id = id;
        return this;
    }

    OrderBuilder lines(OrderLine... orderLines)
    {
        this.orderLines.addAll(Arrays.asList(orderLines));
        return this;
    }

    Order toOrder(){
        // check for initialization of all properties
        if (id == -1 || orderLines.size() == 0){
            throw new IllegalStateException("Order not properly initialized!");
        }

        // build Order
        Order order = new Order(id);
        order.setLines(orderLines);

        return order;
    }
}
