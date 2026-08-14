package com.nexusprocure.warehouse.dto.Request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseRequest {

    @NotBlank(message = "name is required")
    @Size(max = 100)
    private String name;
    @NotBlank(message = "address is required")
    @Size(max = 255)
    private String address;
    @NotNull
    @Positive
    private Integer capacity;
    private Long managerId;
    @NotBlank(message = "contact number is required")
    @Size(max = 20)
    private String contactNumber;
    @NotBlank(message = "email is required")
    @Email
    @Size(max = 255)
    private String email;
    @Size(max = 500)
    private String description;

}
