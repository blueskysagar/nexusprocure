package com.nexusprocure.warehouse.mapper;

import com.nexusprocure.user.entity.User;
import com.nexusprocure.warehouse.dto.Request.WarehouseRequest;
import com.nexusprocure.warehouse.dto.Response.WarehouseResponse;
import com.nexusprocure.warehouse.entity.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "status", ignore = true)
    Warehouse toEntity(WarehouseRequest request);

    @Mapping(target ="managerId", source = "manager.id")
    @Mapping(target = "managerName", source = "manager")
    WarehouseResponse toResponse(Warehouse warehouse);
    default String mapManagerName(User user){
        if(user==null){
            return null;
        }
        return user.getFirstName() + " " + user.getLastName();

    }
}
