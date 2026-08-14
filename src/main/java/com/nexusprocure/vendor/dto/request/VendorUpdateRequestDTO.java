package com.nexusprocure.vendor.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorUpdateRequestDTO {
    @NotBlank(message = "Vendor name is required")
    private String vendorName;
    @NotBlank(message = "Vendor email is required")
    @Email(message = "invalid email format")
    private String email;
    private String phoneNumber;
    private String address;
}
