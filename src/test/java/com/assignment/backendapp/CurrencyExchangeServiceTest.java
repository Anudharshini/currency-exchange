package com.assignment.backendapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.assignment.backendapp.exceptions.DataValidationException;
import com.assignment.backendapp.model.ExchangeRequest;
import com.assignment.backendapp.model.ExchangeResponse;
import com.assignment.backendapp.service.impl.CurrencyExchangeServiceImpl;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class CurrencyExchangeServiceTest {

  @InjectMocks private CurrencyExchangeServiceImpl currencyExchangeService;

  @BeforeEach
  void setUp() {
    currencyExchangeService = new CurrencyExchangeServiceImpl();
  }

  @Test
  void testGetExchangeRate_Success() throws ExecutionException, InterruptedException {
    // Exchange request
    ExchangeRequest exchangeRequest = new ExchangeRequest("EUR", "INR");

    // Mock API response
    ExchangeResponse exchangeResponse =
        ExchangeResponse.builder()
            .exchangeRate(95.168) // EUR to INR
            .usdRate(1.084)
            .build();

    // Perform test
    ExchangeResponse result = currencyExchangeService.getExchangeRate(exchangeRequest);

    // Validate result
    assertEquals(95.168, result.getExchangeRate());
    assertEquals(1.084, result.getUsdRate(), 0.1);
  }

  @Test
  void testGetExchangeRate_Failure() {
    // Expect exception for invalid response
    assertThrows(
        DataValidationException.class,
        () -> {
          currencyExchangeService.getExchangeRate(new ExchangeRequest("XXX", "YYY"));
        });
  }

  @Test
  void testMakeHTTPCall_NullResponse_ThrowsIOException()
      throws ExecutionException, InterruptedException {
    // Mock HttpClient behavior
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://fakeurl.com")).build();
    CompletableFuture<HttpResponse<String>> mockFuture = mock(CompletableFuture.class);

    when(mockFuture.get()).thenReturn(null);

    assertThrows(
        ExecutionException.class,
        () -> {
          CurrencyExchangeServiceImpl.makeHTTPCall("https://fakeurl.com");
        });
  }
}
