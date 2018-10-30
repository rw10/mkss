package org.se.lab;

/**
 * The OrderServiceLoggingDecorator can be used
 * to log the ID and sum of the placed orders.
 *
 * @author rene
 */
class OrderServiceLoggingDecorator extends OrderServiceDecorator {

    /*
     * Constructor
     */
    public OrderServiceLoggingDecorator(OrderService service) {
        super(service);
    }

    /*
     * Interface Operations
     */
    @Override
    public long placeOrder(Order order) {

        // delegate to parent, save return value for post-processing
        long sum = super.placeOrder(order);

        // logging done as post-processing because the sum needs to be calculated first
        Logger.getInstance().log(
                "Order: " + order.getId() + " => " + new Euro(sum)
        );

        return sum;
    }
}
