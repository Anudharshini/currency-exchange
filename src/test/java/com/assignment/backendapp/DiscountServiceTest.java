package com.assignment.backendapp;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.assignment.backendapp.entity.Item;
import com.assignment.backendapp.entity.User;
import com.assignment.backendapp.entity.UserType;
import com.assignment.backendapp.exceptions.DataValidationException;
import com.assignment.backendapp.model.DiscountRequest;
import com.assignment.backendapp.model.DiscountResponse;
import com.assignment.backendapp.model.ExchangeResponse;
import com.assignment.backendapp.service.impl.DiscountServiceImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DiscountServiceTest {

  private DiscountServiceImpl discountService;

  @BeforeEach
  void setUp() {
    discountService = new DiscountServiceImpl();
  }

  @Test
  void testApplyDiscounts_Employee() {
    // Sample request
    DiscountRequest discountRequest =
        new DiscountRequest(
            new User(UserType.EMPLOYEE, 3),
            List.of(
                new Item("Laptop", 1000.0, false),
                new Item("Milk", 50.0, true),
                new Item("Headphones", 200.0, false)));

    // Exchange rate info
    ExchangeResponse exchangeResponse =
        ExchangeResponse.builder().exchangeRate(94.6643).usdRate(1.084).build();

    // Perform test
    DiscountResponse result = discountService.applyDiscounts(discountRequest, exchangeResponse);

    // Validate result
    assertEquals(1250.0, result.getTotalBillAmount());
    assertEquals(360.0, result.getDiscountBreakdown().get("percentageDiscount"));
    assertEquals(36.91, result.getDiscountBreakdown().get("flatDiscount"), 0.01);
  }

  @Test
  void testApplyDiscounts_Affiliate() {
    // Affiliate gets 10% on non-groceries
    DiscountRequest discountRequest =
        new DiscountRequest(
            new User(UserType.AFFILIATE, 2),
            List.of(
                new Item("Laptop", 1000.0, false),
                new Item("Milk", 50.0, true),
                new Item("Headphones", 200.0, false)));

    // Exchange rate info
    ExchangeResponse exchangeResponse =
        ExchangeResponse.builder().exchangeRate(94.6643).usdRate(1.084).build();

    // Perform test
    DiscountResponse result = discountService.applyDiscounts(discountRequest, exchangeResponse);

    // Validate result
    assertEquals(1250.0, result.getTotalBillAmount());
    assertEquals(120.0, result.getDiscountBreakdown().get("percentageDiscount"));
  }

  @Test
  void testApplyDiscounts_NegativePrice() {
    DiscountRequest discountRequest =
        new DiscountRequest(
            new User(UserType.EMPLOYEE, 3), List.of(new Item("Laptop", -1000.0, false)));

    ExchangeResponse exchangeResponse =
        ExchangeResponse.builder().exchangeRate(94.6643).usdRate(1.084).build();

    assertThrows(
        DataValidationException.class,
        () -> {
          discountService.applyDiscounts(discountRequest, exchangeResponse);
        });
  }

  @Test
  void testApplyDiscounts_CustomerWithLoyaltyDiscount() {
    // Customer with >2 years tenure gets a 5% discount (only on non-grocery items)
    DiscountRequest discountRequest =
        new DiscountRequest(
            new User(UserType.CUSTOMER, 3), // Customer for 3 years
            List.of(
                new Item("Laptop", 1000.0, false), // Non-grocery
                new Item("Milk", 50.0, true), // Grocery (no discount)
                new Item("Headphones", 200.0, false) // Non-grocery
                ));

    ExchangeResponse exchangeResponse =
        ExchangeResponse.builder().exchangeRate(94.6643).usdRate(1.084).isTargetUSD(true).build();

    DiscountResponse result = discountService.applyDiscounts(discountRequest, exchangeResponse);

    // Validate results
    double expectedTotalBill = 1250.00;
    double expectedDiscount = 5.0 / 100 * 1200.00; // 5% discount only on non-groceries = 60.0
    double expectedTotalAfterDiscount = expectedTotalBill - expectedDiscount;
    double discount = Math.floor(expectedTotalAfterDiscount / 100) * 5;
    expectedTotalAfterDiscount = expectedTotalAfterDiscount - discount;

    assertEquals(expectedTotalBill, result.getTotalBillAmount());
    assertEquals(expectedTotalAfterDiscount, result.getFinalAmount());
  }
}
