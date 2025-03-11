package com.assignment.backendapp.controller;

import com.assignment.backendapp.model.ApiResponseModel;
import com.assignment.backendapp.model.BillRequest;
import com.assignment.backendapp.model.BillResponse;
import com.assignment.backendapp.service.BillCalculationService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BillController {
  @Autowired private BillCalculationService calculationService;

  @PostMapping("/calculate")
  public ApiResponseModel<BillResponse> calculateBill(@Valid @RequestBody BillRequest bill)
      throws ExecutionException, InterruptedException {

    BillResponse response = calculationService.calculateFinalAmountWithTargetCurrency(bill);
    return new ApiResponseModel.ApiResponseModelBuilder<BillResponse>()
        .data(List.of(response))
        .build();
  }
}
