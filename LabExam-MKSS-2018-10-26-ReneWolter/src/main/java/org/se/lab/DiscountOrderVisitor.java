package org.se.lab;

/**
 * The DiscountOrderVisitor applies the given discount to a Product,
 * or all Products contained in an Order or OrderLine
 *
 * @author rene
 */
class DiscountOrderVisitor implements OrderVisitor {

    private int discount;

    /*
     * Constructor
     */
    DiscountOrderVisitor(int discount){
        // check validity of discount value
        if (discount < 0 || discount > 100){
            throw new IllegalArgumentException(
                    "Illegal Discount value (" +
                    discount +
                    ") : only values from 0 to 100 are allowed!"
            );
        }

        this.discount = discount;
    }


    @Override
    public void visit(Order order) {
        // visit all orderLines
        for (OrderLine orderLine: order.getLines()) {
            visit(orderLine);
        }
    }

    @Override
    public void visit(OrderLine line) {
        // visit the product of the order line
        visit(line.getProduct());
    }

    @Override
    public void visit(Product product) {
        // recalculate the price using the discount

        // needs to be calculated as double
        // otherwise the new price would always become 0
        double newPrice = product.getPrice() * (100-discount) / 100.0;

        // the new price value is cast to long
        product.setPrice((long) newPrice);
    }
}
