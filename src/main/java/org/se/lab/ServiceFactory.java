package org.se.lab;

/**
 * The ServiceFactory defines the Interface for the production of OderService-Objects
 * and contains a (singleton) reference to the factory implementation
 *
 * @author rene
 */
public interface ServiceFactory {

    public final static ServiceFactory INSTANCE = new ServiceFactoryImpl();

    OrderService createOrderService();
}
