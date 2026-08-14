package com.nexusprocure.warehouse.dto.Filter;

import com.nexusprocure.warehouse.entity.WarehouseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseFilterRequest {
    private WarehouseStatus status;
    private Long managerId;
    private String keyword;
    private Integer minCapacity;
    private Integer maxCapacity;

}
