package com.assignment.backendapp.exceptions;

/** The type Data validation exception. */
public class DataValidationException extends BaseException {
  /**
   * Instantiates a new Data validation exception.
   *
   * @param code the code
   * @param message the message
   */
  public DataValidationException(ErrorCode code, String message) {
    super(code, message);
  }

  /**
   * Instantiates a new Data validation exception.
   *
   * @param code the code
   * @param message the message
   * @param cause the cause
   */
  public DataValidationException(ErrorCode code, String message, Throwable cause) {
    super(code, message, cause);
  }
}
