package com.nexusprocure.vendor.mapper;

import com.nexusprocure.vendor.dto.request.VendorRequestDTO;
import com.nexusprocure.vendor.dto.response.VendorResponseDTO;
import com.nexusprocure.vendor.entity.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",  builder = @org.mapstruct.Builder(disableBuilder = true))
public interface VendorMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vendorName", source = "name")
    Vendor toEntity(VendorRequestDTO request);
    VendorResponseDTO toResponse(Vendor vendor);
}
