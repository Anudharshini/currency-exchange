package com.assignment.backendapp.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseModel {

  /** An UUID value which can be used to trace the log of all the events which led to this error */
  private String errorId;

  /** currently code represents HttpStatus code */
  private Integer code;

  /** error represents the error message */
  private Serializable message;
}
