package com.nexusprocure.common.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static org.apache.naming.SelectorContext.prefix;
@Slf4j
@Component
@RequiredArgsConstructor
public class NexusProcureKeyGenerator implements KeyGenerator{
    private final NexusProcureCacheKeyFactory cacheKeyFactory;
    @Override
    public Object generate(
            Object target,
            Method method,
            Object...params
    ){
        log.info(">>> KeyGenerator called. Method={}", method.getName());
        String methodName = method.getName();
        Long id = (long) params[0];
        /*
         * Enterprise Cache Key Strategy
         *
         * Multiple service methods operate on the same Product resource.
         * Regardless of whether we READ or MODIFY the product,
         * they must resolve to the same cache key.
         */

       if(methodName.equals("getProductById")|| methodName.equals("updateProduct")|| methodName.equals("activateProduct")
               || methodName.equals("deactivateProduct")){
           return cacheKeyFactory.productById(id);
       }
       if(methodName.equals("getInventoryById")){
           return cacheKeyFactory.inventoryById(id);
       }
       return methodName + ":" + id;
    }

}
