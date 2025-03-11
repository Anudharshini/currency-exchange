package com.assignment.backendapp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {
  EMPLOYEE("employee"),
  AFFILIATE("affiliate"),
  CUSTOMER("customer");
  private String userLabel;
}
