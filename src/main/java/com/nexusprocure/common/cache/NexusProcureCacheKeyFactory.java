package com.nexusprocure.common.cache;

import org.springframework.stereotype.Component;

@Component
public class NexusProcureCacheKeyFactory {
    public String productById(Long id){
        return CacheKeyConstraints.PRODUCT_PREFIX + ":getById:" + id;
    }

    public String inventoryById(Long id){

        return CacheKeyConstraints.INVENTORY_PREFIX
                + ":getById:"
                + id;
    }


}
