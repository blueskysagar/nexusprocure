package com.nexusprocure.vendor.dto.response;

import com.nexusprocure.vendor.entity.VendorStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class VendorResponseDTO {
    private Long id;
    private String vendorCode;
    private String vendorName;
    private String email;
    private String phone;
    private String address;
    private VendorStatus status;
}
