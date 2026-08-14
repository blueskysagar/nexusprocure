package com.nexusprocure.warehouse.testdata;

import com.nexusprocure.user.entity.User;
import com.nexusprocure.warehouse.dto.Filter.WarehouseFilterRequest;
import com.nexusprocure.warehouse.dto.Request.WarehouseRequest;
import com.nexusprocure.warehouse.dto.Request.WarehouseUpdateRequest;
import com.nexusprocure.warehouse.dto.Response.WarehouseResponse;
import com.nexusprocure.warehouse.entity.Warehouse;
import com.nexusprocure.warehouse.entity.WarehouseStatus;

import java.util.concurrent.atomic.AtomicInteger;

public class WarehouseTestData {
    private WarehouseTestData(){};
    private static final Long WAREHOUSE_ID = 1L;
    private static final Long MANAGER_ID = 1L;

    private static final String WAREHOUSE_CODE = "WH-00001";
    private static final String NAME = "Sydney Warehouse";
    private static final String ADDRESS = "123 George Street, Sydney";
    private static final String CONTACT_NUMBER = "0400123456";
    private static final String EMAIL = "warehouse@nexusprocure.com";
    private static final AtomicInteger counter = new AtomicInteger();
    private static final String DESCRIPTION = "Main warehouse";
    private static final Integer CAPACITY = 1000;
    public static Warehouse entity() {
        return Warehouse.builder()
                .warehouseCode(WAREHOUSE_CODE)
                .name(NAME)
                .address(ADDRESS)
                .status(WarehouseStatus.ACTIVE)
                .capacity(CAPACITY)
                .contactNumber(CONTACT_NUMBER)
                .email(
                        "warehouse" + counter.incrementAndGet() + "@nexusprocure.com"
                )
                .description(DESCRIPTION)
                .build();
    }
    public static Warehouse entityWithId(){

        Warehouse warehouse = entity();

        warehouse.setId(WAREHOUSE_ID);

        return warehouse;
    }
    public static WarehouseRequest request() {

        WarehouseRequest request = new WarehouseRequest();

        request.setName("Sydney Warehouse");
        request.setAddress("123 George Street, Sydney");
        request.setCapacity(1000);
        request.setManagerId(1L);
        request.setContactNumber("0400123456");
        request.setEmail("warehouse@nexusprocure.com");
        request.setDescription("Main warehouse for Sydney region");

        return request;
    }
    public static WarehouseUpdateRequest updateRequest() {

        WarehouseUpdateRequest request = new WarehouseUpdateRequest();

        request.setName("Updated Sydney Warehouse");
        request.setAddress("456 Pitt Street, Sydney");
        request.setManagerId(1L);
        request.setCapacity(2000);
        request.setContactNumber("0411222333");
        request.setEmail("updated@nexusprocure.com");
        request.setDescription("Updated warehouse description");

        return request;
    }
    public static WarehouseResponse response() {

        return WarehouseResponse.builder()
                .warehouseCode("WH-00001")
                .name("Sydney Warehouse")
                .address("123 George Street, Sydney")
                .status(WarehouseStatus.ACTIVE)
                .managerId(1L)
                .managerName("John Doe")
                .capacity(1000)
                .contactNumber("0400123456")
                .email("warehouse@nexusprocure.com")
                .description("Main warehouse for Sydney region")
                .build();
    }
    public static WarehouseFilterRequest emptyFilter() {

        return new WarehouseFilterRequest();
    }

    public static WarehouseFilterRequest statusFilter() {

        WarehouseFilterRequest request = new WarehouseFilterRequest();

        request.setStatus(WarehouseStatus.ACTIVE);

        return request;
    }

    public static WarehouseFilterRequest managerFilter() {

        WarehouseFilterRequest request = new WarehouseFilterRequest();

        request.setManagerId(1L);

        return request;
    }

    public static WarehouseFilterRequest keywordFilter() {

        WarehouseFilterRequest request = new WarehouseFilterRequest();

        request.setKeyword("Sydney");

        return request;
    }

    public static WarehouseFilterRequest capacityRangeFilter() {

        WarehouseFilterRequest request = new WarehouseFilterRequest();

        request.setMinCapacity(500);
        request.setMaxCapacity(2000);

        return request;
    }

    public static WarehouseFilterRequest completeFilter() {

        WarehouseFilterRequest request = new WarehouseFilterRequest();

        request.setStatus(WarehouseStatus.ACTIVE);
        request.setManagerId(1L);
        request.setKeyword("Sydney");
        request.setMinCapacity(500);
        request.setMaxCapacity(2000);

        return request;
    }

}
