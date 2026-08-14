package com.nexusprocure.warehouse.dto.Response;


import com.nexusprocure.warehouse.entity.WarehouseStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseResponse {
    private Long id;
    private String warehouseCode;
    private String name;
    private String address;
    private WarehouseStatus status;
    private Long managerId;
    private String managerName;
    private Integer capacity;
    private String contactNumber;
    private String email;
    private String description;

}
