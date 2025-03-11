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
public class ExchangeRequest implements Serializable {
  private String originalCurrency;
  private String targetCurrency;
}
