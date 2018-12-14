package org.se.lab;

/**
 * The OrderLineBuilder can be used to easily create OrderLine-Objects
 *
 * @author rene
 */
class OrderLineBuilder {

    private int id = -1;
    private int quantity = -1;
    private Product product = null;

    /*
     * Operations
     */
    OrderLineBuilder id(int id){
        // validity check
        if (id < 0){
            throw new IllegalArgumentException("Negative ID is not allowed!");
        }

        this.id = id;
        return this;
    }

    OrderLineBuilder quantity(int quantity){
        // validity check
        if (quantity < 0){
            throw new IllegalArgumentException("Negative quantity is not allowed!");
        }
        this.quantity = quantity;
        return this;
    }


    OrderLineBuilder product(int id, String description, long price){
        // validity check
        if (description == null){
            throw new IllegalArgumentException("Description is null");
        }

        product = new Product(id, description, price);
        return this;
    }

    OrderLine toOrderLine(){
        // check for initialization of all properties
        if (id == -1 || quantity == -1 || product == null){
            throw new IllegalStateException("Orderline not properly initialized!");
        }

        return new OrderLine(id, quantity, product);
    }

}
