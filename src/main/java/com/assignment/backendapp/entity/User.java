package com.assignment.backendapp.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
  @NotBlank(message = "User type cannot be blank.")
  private UserType userType;

  @NotNull(message = "Tenure in years cannot be null.")
  @Min(value = 0, message = "Tenure in years cannot be negative.")
  private int tenureInYears; // For checking if the customer is loyal
}
