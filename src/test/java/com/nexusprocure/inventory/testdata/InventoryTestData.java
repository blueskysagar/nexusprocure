package com.nexusprocure.inventory.testdata;

import com.nexusprocure.common.Response.PageResponse;
import com.nexusprocure.inventory.dto.request.InventoryFilterRequest;
import com.nexusprocure.inventory.dto.request.InventoryRequest;
import com.nexusprocure.inventory.dto.response.InventoryResponse;
import com.nexusprocure.inventory.dto.update.InventoryUpdateRequest;
import com.nexusprocure.inventory.entity.Inventory;
import com.nexusprocure.inventory.enums.InventoryStatus;
import com.nexusprocure.product.entity.Product;
import com.nexusprocure.warehouse.entity.Warehouse;

import java.util.List;

public final class InventoryTestData {
    private InventoryTestData(){}
    private static final Long DEFAULT_INVENTORY_ID = 1L;
    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final String DEFAULT_PRODUCT_CODE = "PRD-00001";

    private static final Long DEFAULT_WAREHOUSE_ID = 1L;
    private static final String DEFAULT_WAREHOUSE_CODE = "WH-00001";
    private static final String DEFAULT_WAREHOUSE_NAME = "Sydney Warehouse";

    private static final Integer DEFAULT_QUANTITY = 50;
    private static final Integer DEFAULT_RESERVED_QUANTITY = 5;
    private static final Integer DEFAULT_AVAILABLE_QUANTITY = 45;

    private static final Integer DEFAULT_MINIMUM_STOCK = 10;
    private static final Integer DEFAULT_MAXIMUM_STOCK = 100;

    private static final InventoryStatus DEFAULT_STATUS = InventoryStatus.ACTIVE;

    // ---------- Entity ----------

    public static Inventory entity() {
        Inventory inventory = new Inventory();
        inventory.setQuantity(DEFAULT_QUANTITY);
        inventory.setReservedQuantity(DEFAULT_RESERVED_QUANTITY);
        inventory.setMinimumStock(DEFAULT_MINIMUM_STOCK);
        inventory.setMaximumStock(DEFAULT_MAXIMUM_STOCK);
        inventory.setInventoryStatus(DEFAULT_STATUS);

        return inventory;
    }
    public static Inventory entityWithId() {

        Inventory inventory = entity();

        inventory.setId(DEFAULT_INVENTORY_ID);

        return inventory;
    }
    // ---------- Create Request ----------

    public static InventoryRequest request() {

        InventoryRequest request = new InventoryRequest();

        request.setProductId(DEFAULT_PRODUCT_ID);
        request.setWarehouseId(DEFAULT_WAREHOUSE_ID);
        request.setQuantity(DEFAULT_QUANTITY);
        request.setMinimumStock(DEFAULT_MINIMUM_STOCK);
        request.setMaximumStock(DEFAULT_MAXIMUM_STOCK);

        return request;
    }
    public static Inventory savedEntity(Product product, Warehouse warehouse) {

        Inventory inventory = entity();

        inventory.setProduct(product);
        inventory.setWarehouse(warehouse);

        return inventory;
    }

    public static InventoryUpdateRequest updateRequest() {

        InventoryUpdateRequest request = new InventoryUpdateRequest();

        request.setMinimumStock(DEFAULT_MINIMUM_STOCK);
        request.setMaximumStock(DEFAULT_MAXIMUM_STOCK);

        return request;
    }

    // ---------- Response ----------

    public static InventoryResponse response() {

        InventoryResponse response = new InventoryResponse();

        response.setProductId(DEFAULT_PRODUCT_ID);
        response.setProductCode(DEFAULT_PRODUCT_CODE);

        response.setWarehouseId(DEFAULT_WAREHOUSE_ID);
        response.setWarehouseCode(DEFAULT_WAREHOUSE_CODE);
        response.setWarehouseName(DEFAULT_WAREHOUSE_NAME);

        response.setQuantity(DEFAULT_QUANTITY);
        response.setReservedQuantity(DEFAULT_RESERVED_QUANTITY);
        response.setAvailableQuantity(DEFAULT_AVAILABLE_QUANTITY);

        response.setMinimumStock(DEFAULT_MINIMUM_STOCK);
        response.setMaximumStock(DEFAULT_MAXIMUM_STOCK);

        return response;
    }

    public static PageResponse<InventoryResponse> pageResponse() {

        PageResponse<InventoryResponse> response =
                new PageResponse<>();

        response.setContent(List.of(response(), response()));
        response.setPage(0);
        response.setSize(10);
        response.setTotalElements(2);
        response.setTotalPages(1);

        return response;
    }
    public static InventoryFilterRequest emptyFilter() {
        return new InventoryFilterRequest();
    }

    public static InventoryFilterRequest statusFilter() {

        InventoryFilterRequest request = new InventoryFilterRequest();

        request.setInventoryStatus(DEFAULT_STATUS);

        return request;
    }

    public static InventoryFilterRequest productFilter() {

        InventoryFilterRequest request = new InventoryFilterRequest();

        request.setProductId(DEFAULT_PRODUCT_ID);

        return request;
    }

    public static InventoryFilterRequest warehouseFilter() {

        InventoryFilterRequest request = new InventoryFilterRequest();

        request.setWarehouseId(DEFAULT_WAREHOUSE_ID);

        return request;
    }

    public static InventoryFilterRequest quantityRangeFilter() {

        InventoryFilterRequest request = new InventoryFilterRequest();

        request.setMinimumQuantity(10);
        request.setMaximumQuantity(100);

        return request;
    }

    public static InventoryFilterRequest completeFilter() {

        InventoryFilterRequest request = new InventoryFilterRequest();

        request.setProductId(DEFAULT_PRODUCT_ID);
        request.setWarehouseId(DEFAULT_WAREHOUSE_ID);
        request.setInventoryStatus(DEFAULT_STATUS);
        request.setMinimumQuantity(10);
        request.setMaximumQuantity(100);

        return request;
    }


}
