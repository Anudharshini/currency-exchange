package com.assignment.backendapp.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
  @NotBlank(message = "Item name cannot be blank.")
  private String name;

  @NotNull(message = "Item price cannot be null.")
  @Min(value = 0, message = "Item price must be non-negative.")
  private double price;

  @NotNull(message = "Grocery field cannot be null.")
  private boolean isGrocery;
}
