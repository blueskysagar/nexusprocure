package com.nexusprocure.warehouse.security;

import com.nexusprocure.authentication.security.SecurityUtils;
import com.nexusprocure.user.entity.Role;
import jakarta.persistence.Column;
import org.springframework.stereotype.Component;

@Component("warehouseSecurity")
public class WarehouseSecurity {
    public boolean canManageWarehouse(){
        return SecurityUtils.hasRole(Role.ADMIN);
    }
}
