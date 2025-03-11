package com.assignment.backendapp.exceptions;

public class AuthenticationFailureException extends BaseException {
  /**
   * Instantiates a new Internal io exception.
   *
   * @param code the code
   * @param message the message
   */
  public AuthenticationFailureException(ErrorCode code, String message) {
    super(code, message);
  }
}
