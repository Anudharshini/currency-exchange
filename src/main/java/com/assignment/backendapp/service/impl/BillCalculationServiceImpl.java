package com.assignment.backendapp.service.impl;

import com.assignment.backendapp.entity.Item;
import com.assignment.backendapp.exceptions.DataValidationException;
import com.assignment.backendapp.exceptions.ErrorCode;
import com.assignment.backendapp.model.BillRequest;
import com.assignment.backendapp.model.BillResponse;
import com.assignment.backendapp.model.DiscountRequest;
import com.assignment.backendapp.model.DiscountResponse;
import com.assignment.backendapp.model.ExchangeRequest;
import com.assignment.backendapp.model.ExchangeResponse;
import com.assignment.backendapp.service.BillCalculationService;
import com.assignment.backendapp.service.CurrencyExchangeService;
import com.assignment.backendapp.service.DiscountService;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillCalculationServiceImpl implements BillCalculationService {

  @Autowired private CurrencyExchangeService exchangeService;
  @Autowired private DiscountService discountService;

  public BillResponse calculateFinalAmountWithTargetCurrency(BillRequest billRequest)
      throws ExecutionException, InterruptedException {
    for (Item item : billRequest.getItems()) {
      if (item.getPrice() < 0) {
        throw new DataValidationException(
            ErrorCode.DATA_VALIDATION_ERROR, "Item price cannot be negative: " + item.getName());
      }
    }
    ExchangeRequest exchangeRequest =
        ExchangeRequest.builder()
            .originalCurrency(billRequest.getOriginalCurrency())
            .targetCurrency(billRequest.getTargetCurrency())
            .build();
    ExchangeResponse exchangeResponse = exchangeService.getExchangeRate(exchangeRequest);

    DiscountRequest discountRequest =
        DiscountRequest.builder()
            .user(billRequest.getUser())
            .itemList(billRequest.getItems())
            .build();
    DiscountResponse discountResponse =
        discountService.applyDiscounts(discountRequest, exchangeResponse);

    double totalAmount = discountResponse.getFinalAmount() * exchangeResponse.getExchangeRate();

    return BillResponse.builder()
        .originalBillAmount(discountResponse.getTotalBillAmount())
        .finalCost(totalAmount)
        .originalCurrency(exchangeRequest.getOriginalCurrency())
        .targetCurrency(exchangeRequest.getTargetCurrency())
        .build();
  }
}
