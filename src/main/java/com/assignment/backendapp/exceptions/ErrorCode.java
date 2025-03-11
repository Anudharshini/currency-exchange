package com.assignment.backendapp.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ErrorCode enum will help us to uniquely identify each and every exception which has been manually
 * thrown in the execution flow
 */
@AllArgsConstructor
@Getter
public enum ErrorCode {
  INPUT_OUTPUT_ERROR(1000, "Failed to fetch output"),
  DATA_VALIDATION_ERROR(8001, "Data Validation Error occurred"),
  DATA_PARSE_ERROR(8002, "Data Parsing Error occurred"),
  OPERATION_NOT_SUPPORTED_ERROR(8003, "Operation not supported"),
  RESPONSE_FAILURE(1010, "Response failure from third party"),
  AUTH_INVALID_API_KEY(1011, "Invalid API key");

  private final int value;
  private final String reason;
}
