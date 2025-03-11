package com.assignment.backendapp.model;

import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;

@Data
@Builder
@AllArgsConstructor
@Generated
public class DiscountResponse implements Serializable {
  private double totalBillAmount;
  private double finalAmount;
  private double currencyConversionRate;
  private Map<String, Double> discountBreakdown;
}
