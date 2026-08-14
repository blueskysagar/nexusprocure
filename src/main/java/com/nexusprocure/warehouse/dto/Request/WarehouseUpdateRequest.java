package com.nexusprocure.warehouse.dto.Request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseUpdateRequest {
    @NotBlank(message = "warehouse name is required")
   private String name;
    @NotBlank(message = "address is required")
   private String address;
    @NotNull(message = "manager is required")
   private Long managerId;
    @NotNull(message = "capacity is required")
    @Min(value = 1, message = "Capacity must be greater than zero")
   private Integer capacity;
  @NotBlank(message = "contact number is required")
   private String contactNumber;
  @NotBlank(message = "email is required")
  @Email(message = "email must be valid")
   private String email;
   private String description;

}
