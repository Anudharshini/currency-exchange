package com.assignment.backendapp.model;

import com.assignment.backendapp.entity.Item;
import com.assignment.backendapp.entity.User;
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
public class DiscountRequest implements Serializable {
  private User user;
  private List<Item> itemList;
}
