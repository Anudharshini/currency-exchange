package com.assignment.backendapp.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;

@Data
@Builder
@AllArgsConstructor
@Generated
public class BillResponse implements Serializable {
  private double originalBillAmount;
  private double finalCost;
  private String originalCurrency;
  private String targetCurrency;
}
