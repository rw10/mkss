package org.se.lab;

import org.se.lab.legacy.OrderProcessor;
import org.se.lab.legacy.OrderRecord;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rene
 */
class OrderServiceAdapter implements OrderService {

    OrderProcessor legacyImpl;

    /*
     * Constructor
     */
    OrderServiceAdapter(OrderProcessor legacyOrderProcessor){
        // validity check
        if (legacyOrderProcessor == null){
            throw new IllegalArgumentException("Invalid OrderProcessor reference!");
        }

        legacyImpl = legacyOrderProcessor;
    }

    /*
     * Interface Operations
     */
    @Override
    public long placeOrder(Order order) {
        // validity check
        if (order == null){
            throw new IllegalArgumentException("Invalid Order reference!");
        }

        // copy OrderLines to fit legacy structure
        List<OrderRecord> table = new ArrayList<>();
        for (OrderLine orderLine : order.getLines()){

            // construct OrderRecord
            OrderRecord record = new OrderRecord();
            record.id = orderLine.getId();
            record.description = orderLine.getProduct().getDescription();
            record.price = orderLine.getProduct().getPrice();
            record.quantity = orderLine.getQuantity();

            // add to table
            table.add(record);
        }

        // call legacy function
        long totalPrice = 0;
        try {
            totalPrice = legacyImpl.process(table);
        } catch (SQLException e) {
            // catch checked exception and throw a Runtime Exception instead

            // maybe print the stack trace as the information of the exception would be lost otherwise
            //e.printStackTrace();

            throw new ServiceException();
        }

        return totalPrice;
    }
}
