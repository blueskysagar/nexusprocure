package com.nexusprocure.vendor.dto.request;

import com.nexusprocure.vendor.entity.VendorStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorRequestDTO {
    @NotBlank(message = "Vendor code is required")
    private String vendorCode;
    @NotBlank(message = "name is required")
    private String name;
    @Email(message = "invalid email format")
    @NotBlank(message = "email is required")
    private String email;
    private String phone;
    private String address;
    private VendorStatus status;

}
