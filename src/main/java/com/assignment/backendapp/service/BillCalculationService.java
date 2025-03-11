package com.assignment.backendapp.service;

import com.assignment.backendapp.model.BillRequest;
import com.assignment.backendapp.model.BillResponse;
import java.util.concurrent.ExecutionException;

public interface BillCalculationService {
  BillResponse calculateFinalAmountWithTargetCurrency(BillRequest bill)
      throws ExecutionException, InterruptedException;
}
