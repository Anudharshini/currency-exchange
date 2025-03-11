package com.assignment.backendapp.model;

import com.assignment.backendapp.entity.Item;
import com.assignment.backendapp.entity.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;

@Data
@Builder
@AllArgsConstructor
@Generated
public class BillRequest implements Serializable {
  @NotEmpty(message = "Items list cannot be empty.")
  private List<@NotNull(message = "Item cannot be null.") Item> items;

  @NotNull(message = "Original currency cannot be null.")
  private String originalCurrency;

  @NotNull(message = "Target currency cannot be null.")
  private String targetCurrency;

  @NotNull(message = "User details cannot be null.")
  private User user;
}
