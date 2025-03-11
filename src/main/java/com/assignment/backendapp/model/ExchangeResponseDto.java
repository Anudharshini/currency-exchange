package com.assignment.backendapp.model;

import com.assignment.backendapp.utils.JSONUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Generated
public class ExchangeResponseDto implements Serializable {
  @JsonProperty("result")
  private String result;

  @JsonProperty("time_last_update_utc")
  private String timeLastUpdateUtc;

  @JsonProperty("base_code")
  private String baseCode;

  @JsonProperty("conversion_rates")
  private Map<String, Double> conversionRates;

  public static ExchangeResponseDto fromJSON(String inputString) {
    return JSONUtils.fromJson(inputString, ExchangeResponseDto.class);
  }
}
