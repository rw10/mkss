package org.se.lab;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test cases for invalid parameters
 *
 * @author rene
 */
public class InvalidInputTest
{
	private OrderService service;

	@Before
	public void setup()
	{
		service = ServiceFactory.INSTANCE.createOrderService();
	}

	@Test(expected=IllegalArgumentException.class)
	public void testInvalidProcessor()
	{
		service.placeOrder(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testInvalidOrder()
	{
		OrderServiceAdapter adapter = new OrderServiceAdapter(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testInvalidService()
	{
		OrderServiceLoggingDecorator decorator = new OrderServiceLoggingDecorator(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNegativeCents()
	{
		Euro euro = new Euro(-1);
	}

	@Test
	public void testSingleDigitCents()
	{
		Euro euro = new Euro(2);
		assertTrue(euro.toString().equals("EUR 0.02"));
		Euro euro2 = new Euro(202);
		assertTrue(euro2.toString().equals("EUR 2.02"));

	}

	@Test(expected=IllegalArgumentException.class)
	public void testNegativeDiscount()
	{
		DiscountOrderVisitor discountOrderVisitor = new DiscountOrderVisitor(-1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testDiscountTooHigh()
	{
		DiscountOrderVisitor discountOrderVisitor = new DiscountOrderVisitor(101);
	}


	@Test(expected=IllegalArgumentException.class)
	public void testOrderBuilderIDcheck() {
		new OrderBuilder().id(-10);
	}

	@Test(expected=IllegalStateException.class)
	public void testOrderBuilderInitcheck() {
		OrderBuilder orderBuilder = new OrderBuilder().id(10);
		orderBuilder.toOrder();
	}

	@Test(expected=IllegalStateException.class)
	public void testOrderBuilderInitcheck2() {
		OrderBuilder orderBuilder = new OrderBuilder().lines(
				new OrderLine(1,1, new Product(1,"desc",1))
		);
		orderBuilder.toOrder();
	}


	@Test(expected=IllegalArgumentException.class)
	public void testOrderLineBuilderIDcheck() {
		new OrderLineBuilder().id(-10);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testOrderLineBuilderQuantitycheck() {
		new OrderLineBuilder().quantity(-10);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testOrderLineBuilderProductcheck() {
		new OrderLineBuilder().product(1, null, 1);
	}

	@Test(expected=IllegalStateException.class)
	public void testOrderLineBuilderInitcheck() {
		OrderLineBuilder orderlineBuilder = new OrderLineBuilder().id(10).quantity(10);
		orderlineBuilder.toOrderLine();
	}

	@Test(expected=IllegalStateException.class)
	public void testOrderLineBuilderInitcheck2() {
		OrderLineBuilder orderlineBuilder = new OrderLineBuilder().id(10).product(1,"desc",1);
		orderlineBuilder.toOrderLine();
	}

	@Test(expected=IllegalStateException.class)
	public void testOrderLineBuilderInitcheck3() {
		OrderLineBuilder orderlineBuilder = new OrderLineBuilder().quantity(10).product(1,"desc",1);
		orderlineBuilder.toOrderLine();
	}
}
