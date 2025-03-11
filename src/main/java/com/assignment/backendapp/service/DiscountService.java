package com.assignment.backendapp.service;

import com.assignment.backendapp.model.DiscountRequest;
import com.assignment.backendapp.model.DiscountResponse;
import com.assignment.backendapp.model.ExchangeResponse;

public interface DiscountService {
  DiscountResponse applyDiscounts(
      DiscountRequest discountRequest, ExchangeResponse exchangeResponse);
}
