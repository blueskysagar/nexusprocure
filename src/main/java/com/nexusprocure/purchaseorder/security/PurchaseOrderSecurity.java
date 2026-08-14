package com.nexusprocure.purchaseorder.security;

import com.nexusprocure.authentication.security.SecurityUtils;
import com.nexusprocure.user.entity.Role;
import org.springframework.stereotype.Component;

@Component("purchaseOrderSecurity")
public class PurchaseOrderSecurity {
public boolean canCreate()
{
    return SecurityUtils.hasRole(Role.ADMIN);
}
public boolean canRead(Long purchaseOrderId){
    return SecurityUtils.hasAnyRole(Role.USER, Role.ADMIN);
}
public boolean canUpdate(Long purchaseOrderId){
    return SecurityUtils.hasRole(Role.ADMIN);
}
public boolean canApprove(Long purchaseOrderId){
    return SecurityUtils.hasRole(Role.ADMIN);
}
public boolean canSend(Long purchaseOrderId){
    return SecurityUtils.hasRole(Role.ADMIN);
}
public boolean canComplete(Long purchaseOrderId){
    return SecurityUtils.hasRole(Role.ADMIN);
}
public boolean canCancel(Long purchaseOrderId){
    return SecurityUtils.hasRole(Role.ADMIN);
}
}

