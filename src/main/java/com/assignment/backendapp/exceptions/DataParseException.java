package com.assignment.backendapp.exceptions;

public class DataParseException extends BaseException {

  /**
   * Constructs a new FileParseException exception with the specified detail message and code
   *
   * @param code the {@link ErrorCode} to identify this exception instance
   * @param message the detailed message about the exception
   */
  public DataParseException(ErrorCode code, String message) {
    super(code, message);
  }

  /**
   * Constructs a new File Parse Exception with the specified detail message, cause and code
   *
   * <p>Note that the detail message associated with {@link #getCause()} is <i>not</i> automatically
   * incorporated in this runtime exception's detail message.
   *
   * @param code the error code
   * @param message the detail message
   * @param cause the cause (A <tt>null</tt> value is permitted, and indicates that the cause is
   */
  public DataParseException(ErrorCode code, String message, Throwable cause) {
    super(code, message, cause);
  }
}
