package com.assignment.backendapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.assignment.backendapp.entity.Item;
import com.assignment.backendapp.entity.User;
import com.assignment.backendapp.entity.UserType;
import com.assignment.backendapp.exceptions.DataValidationException;
import com.assignment.backendapp.model.BillRequest;
import com.assignment.backendapp.model.BillResponse;
import com.assignment.backendapp.model.DiscountRequest;
import com.assignment.backendapp.model.DiscountResponse;
import com.assignment.backendapp.model.ExchangeRequest;
import com.assignment.backendapp.model.ExchangeResponse;
import com.assignment.backendapp.service.CurrencyExchangeService;
import com.assignment.backendapp.service.DiscountService;
import com.assignment.backendapp.service.impl.BillCalculationServiceImpl;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class BillCalculationServiceTest {

  @InjectMocks private BillCalculationServiceImpl billCalculationService;

  @Mock private CurrencyExchangeService exchangeService;

  @Mock private DiscountService discountService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCalculateFinalAmount() throws ExecutionException, InterruptedException {
    // Sample bill request
    BillRequest billRequest =
        BillRequest.builder()
            .items(
                List.of(
                    new Item("Laptop", 1000.0, false),
                    new Item("Milk", 50.0, true),
                    new Item("Headphones", 200.0, false)))
            .originalCurrency("EUR")
            .targetCurrency("INR")
            .user(new User(UserType.EMPLOYEE, 3))
            .build();

    // Mock Exchange Response
    ExchangeResponse exchangeResponse =
        ExchangeResponse.builder()
            .exchangeRate(94.6643) // EUR to INR
            .usdRate(1.084)
            .build();

    // Mock Discount Response
    DiscountResponse discountResponse =
        DiscountResponse.builder()
            .totalBillAmount(1250.0)
            .finalAmount(853.09) // After discounts
            .build();

    // Define behavior of mocks
    when(exchangeService.getExchangeRate(new ExchangeRequest("EUR", "INR")))
        .thenReturn(exchangeResponse);
    when(discountService.applyDiscounts(
            new DiscountRequest(billRequest.getUser(), billRequest.getItems()), exchangeResponse))
        .thenReturn(discountResponse);

    // Perform test
    BillResponse result =
        billCalculationService.calculateFinalAmountWithTargetCurrency(billRequest);

    // Validate result
    assertEquals(1250.0, result.getOriginalBillAmount());
    assertEquals(853.09 * 94.6643, result.getFinalCost(), 0.01); // Allow minor rounding differences
    assertEquals("EUR", result.getOriginalCurrency());
    assertEquals("INR", result.getTargetCurrency());
  }

  @Test
  void testCalculateFinalAmount_EmptyItemList() {
    BillRequest billRequest =
        BillRequest.builder()
            .items(Collections.emptyList())
            .originalCurrency("EUR")
            .targetCurrency("INR")
            .user(new User(UserType.EMPLOYEE, 3))
            .build();

    assertThrows(
        NullPointerException.class,
        () -> {
          billCalculationService.calculateFinalAmountWithTargetCurrency(billRequest);
        });
  }

  @Test
  void testCalculateFinalAmount_DiscountServiceException()
      throws ExecutionException, InterruptedException {
    BillRequest billRequest =
        BillRequest.builder()
            .items(List.of(new Item("Laptop", 1000.0, false)))
            .originalCurrency("EUR")
            .targetCurrency("INR")
            .user(new User(UserType.EMPLOYEE, 3))
            .build();

    ExchangeResponse exchangeResponse =
        ExchangeResponse.builder().exchangeRate(94.6643).usdRate(1.084).build();

    // Mock successful exchange rate response
    when(exchangeService.getExchangeRate(new ExchangeRequest("EUR", "INR")))
        .thenReturn(exchangeResponse);

    // Mock Discount Service to throw an exception
    when(discountService.applyDiscounts(
            new DiscountRequest(billRequest.getUser(), billRequest.getItems()), exchangeResponse))
        .thenThrow(new RuntimeException("Discount calculation failed"));

    assertThrows(
        RuntimeException.class,
        () -> {
          billCalculationService.calculateFinalAmountWithTargetCurrency(billRequest);
        });
  }

  @Test
  void testCalculateFinalAmount_NegativeItemPrice() {
    BillRequest billRequest =
        BillRequest.builder()
            .items(List.of(new Item("Laptop", -1000.0, false)))
            .originalCurrency("EUR")
            .targetCurrency("INR")
            .user(new User(UserType.EMPLOYEE, 3))
            .build();

    assertThrows(
        DataValidationException.class,
        () -> {
          billCalculationService.calculateFinalAmountWithTargetCurrency(billRequest);
        });
  }
}
