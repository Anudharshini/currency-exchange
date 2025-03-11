package com.assignment.backendapp.service.impl;

import com.assignment.backendapp.entity.Item;
import com.assignment.backendapp.exceptions.DataValidationException;
import com.assignment.backendapp.exceptions.ErrorCode;
import com.assignment.backendapp.model.DiscountRequest;
import com.assignment.backendapp.model.DiscountResponse;
import com.assignment.backendapp.model.ExchangeResponse;
import com.assignment.backendapp.service.DiscountService;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class DiscountServiceImpl implements DiscountService {

  public DiscountResponse applyDiscounts(
      DiscountRequest discountRequest, ExchangeResponse exchangeResponse) {
    // Step 1: Get total bill amount
    double totalAmount = getTotalAmount(discountRequest.getItemList());

    // Step 2: Apply percentage-based discount only on non-grocery items
    double nonGroceryTotal = getNonGroceryTotal(discountRequest.getItemList());
    double percentageDiscount = calculatePercentageDiscount(discountRequest, nonGroceryTotal);
    double discountedNonGroceryTotal = nonGroceryTotal - percentageDiscount;

    // Step 3: Get grocery total (no discount applied)
    double groceryTotal = totalAmount - nonGroceryTotal;

    // Step 4: Compute total after percentage discount
    double totalAfterDiscount = discountedNonGroceryTotal + groceryTotal;

    // Step 5: Apply $5 per $100 flat discount (currency-aware)
    double finalAmount = applyFlatDiscount(totalAfterDiscount, exchangeResponse);

    Map<String, Double> discountBreakdown = new HashMap<>();
    discountBreakdown.put("percentageDiscount", percentageDiscount);
    discountBreakdown.put("flatDiscount", (totalAfterDiscount - finalAmount));

    if (finalAmount < 0) {
      throw new DataValidationException(
          ErrorCode.DATA_VALIDATION_ERROR, "Final bill amount cannot be negative: " + finalAmount);
    }
    DecimalFormat df = new DecimalFormat("0.00");
    DiscountResponse discountResponse =
        DiscountResponse.builder()
            .totalBillAmount(totalAmount)
            .finalAmount(Double.parseDouble(df.format(finalAmount)))
            .discountBreakdown(discountBreakdown)
            .build();

    return discountResponse;
  }

  // Calculate total bill amount (including groceries)
  private double getTotalAmount(List<Item> items) {
    return items.stream().mapToDouble(Item::getPrice).sum();
  }

  // Calculate total amount of non-grocery items
  private double getNonGroceryTotal(List<Item> items) {
    return items.stream()
        .filter(item -> !item.isGrocery()) // Exclude groceries
        .mapToDouble(Item::getPrice)
        .sum();
  }

  private double calculatePercentageDiscount(
      DiscountRequest discountRequest, double nonGroceryTotal) {
    double discountRate =
        switch (discountRequest.getUser().getUserType()) {
          case EMPLOYEE -> 0.30;
          case AFFILIATE -> 0.10;
          case CUSTOMER -> (discountRequest.getUser().getTenureInYears() > 2) ? 0.05 : 0.0;
          default -> 0.0;
        };

    return nonGroceryTotal * discountRate;
  }

  private double applyFlatDiscount(double totalAfterDiscount, ExchangeResponse exchangeResponse) {
    if (exchangeResponse.isTargetUSD()) {
      if (totalAfterDiscount > 100) {
        double discount = Math.floor(totalAfterDiscount / 100) * 5;
        totalAfterDiscount = totalAfterDiscount - discount;
      }
    } else {
      // Convert to USD
      double amountInUSD = totalAfterDiscount / exchangeResponse.getUsdRate();
      double discount = 0;
      if (amountInUSD > 100) {
        discount = (Math.floor(amountInUSD / 100) * 5) / exchangeResponse.getUsdRate();
      }
      // Convert back to original currency
      totalAfterDiscount = totalAfterDiscount - discount;
    }
    return totalAfterDiscount;
  }
}
