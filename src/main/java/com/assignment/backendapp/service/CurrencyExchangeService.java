package com.assignment.backendapp.service;

import com.assignment.backendapp.model.ExchangeRequest;
import com.assignment.backendapp.model.ExchangeResponse;
import java.util.concurrent.ExecutionException;

public interface CurrencyExchangeService {
  ExchangeResponse getExchangeRate(ExchangeRequest exchangeRequest)
      throws ExecutionException, InterruptedException;
}
