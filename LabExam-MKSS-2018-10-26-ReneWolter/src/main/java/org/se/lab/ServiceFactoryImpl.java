package org.se.lab;

import org.se.lab.legacy.OrderProcessor;

/**
 * The ServiceFactoryImpl realizes a factory for creating OrderService-Objects
 *
 * @author rene
 */
class ServiceFactoryImpl implements ServiceFactory {

    /*
     * Interface Operations
     */
    @Override
    public OrderService createOrderService() {

        // Decorator Implementation
        return new OrderServiceLoggingDecorator(

                // Adapter Implementation
                new OrderServiceAdapter(

                        // Legacy Implementation
                        new OrderProcessor()
                )
        );
    }
}
