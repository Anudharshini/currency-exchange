package com.assignment.backendapp.service.impl;

import com.assignment.backendapp.exceptions.DataValidationException;
import com.assignment.backendapp.exceptions.ErrorCode;
import com.assignment.backendapp.exceptions.IOException;
import com.assignment.backendapp.model.ExchangeRequest;
import com.assignment.backendapp.model.ExchangeResponse;
import com.assignment.backendapp.model.ExchangeResponseDto;
import com.assignment.backendapp.service.CurrencyExchangeService;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {
  private static final String API_URL =
      "https://v6.exchangerate-api.com/v6/{api_key}/latest/{base_currency}";
  private static final String API_KEY =
      "a158e6d78a140c36e708bc99"; // Replace with your actual API key
  private static final HttpClient httpClient = HttpClient.newHttpClient();

  @Cacheable(
      value = "exchangeRates",
      key = "#exchangeRequest.originalCurrency + '_' + #exchangeRequest.targetCurrency")
  public ExchangeResponse getExchangeRate(ExchangeRequest exchangeRequest)
      throws ExecutionException, InterruptedException {
    String url =
        API_URL
            .replace("{base_currency}", exchangeRequest.getOriginalCurrency())
            .replace("{api_key}", API_KEY);

    ExchangeResponseDto responseDto = makeHTTPCall(url);
    return ExchangeResponse.builder()
        .exchangeRate(
            responseDto
                .getConversionRates()
                .getOrDefault(exchangeRequest.getTargetCurrency(), -1.0))
        .isTargetUSD("USD".equalsIgnoreCase(exchangeRequest.getTargetCurrency()))
        .usdRate(responseDto.getConversionRates().getOrDefault("USD", -1.0))
        .originalExchangeRate(
            responseDto
                .getConversionRates()
                .getOrDefault(exchangeRequest.getOriginalCurrency(), -1.0))
        .build();
  }

  public static ExchangeResponseDto makeHTTPCall(String uri)
      throws ExecutionException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();
    CompletableFuture<HttpResponse<String>> responseCompletableFuture =
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    if (Objects.isNull(responseCompletableFuture.get())) {
      throw new IOException(ErrorCode.INPUT_OUTPUT_ERROR, "Invalid response obtained ");
    }
    try {
      ExchangeResponseDto exchangeResponseDto =
          ExchangeResponseDto.fromJSON(responseCompletableFuture.get().body());
      if (!"success".equalsIgnoreCase(exchangeResponseDto.getResult())) {
        throw new DataValidationException(
            ErrorCode.RESPONSE_FAILURE, "Failure in exchangerate-api");
      }
      return exchangeResponseDto;
    } catch (Exception e) {
      if (e instanceof DataValidationException) {
        throw e;
      }
      throw new DataValidationException(
          ErrorCode.DATA_VALIDATION_ERROR, "Error processing exchange rate response", e);
    }
  }
}
