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
public class ExchangeResponse implements Serializable {

  private double exchangeRate;
  private boolean isTargetUSD;
  private double usdRate;
  private double originalExchangeRate;
}
