package com.nexusprocure.inventory.integration;

import com.nexusprocure.integration.BaseIntegrationTest;
import com.nexusprocure.inventory.dto.request.InventoryRequest;
import com.nexusprocure.inventory.dto.update.InventoryUpdateRequest;
import com.nexusprocure.inventory.entity.Inventory;
import com.nexusprocure.inventory.enums.InventoryStatus;
import com.nexusprocure.inventory.repository.InventoryRepository;
import com.nexusprocure.inventory.testdata.InventoryTestData;
import com.nexusprocure.product.entity.Category;
import com.nexusprocure.product.entity.Product;
import com.nexusprocure.product.repository.CategoryRepository;
import com.nexusprocure.product.repository.ProductRepository;
import com.nexusprocure.product.testdata.CategoryTestData;
import com.nexusprocure.product.testdata.ProductTestData;
import com.nexusprocure.user.repository.UserRepository;
import com.nexusprocure.warehouse.entity.Warehouse;
import com.nexusprocure.warehouse.repository.WarehouseRepository;
import com.nexusprocure.warehouse.testdata.WarehouseTestData;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
public class InventoryIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private  CategoryRepository categoryRepository;
    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private  InventoryRepository inventoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldCreateInventorySuccessfully()throws Exception{
        //Arrange
        Category category = CategoryTestData.entity();
        category = categoryRepository.save(category);

        Product product = ProductTestData.entity();
        product.setCategory(category);
        product = productRepository.save(product);

        Warehouse warehouse = WarehouseTestData.entity();
        warehouse = warehouseRepository.save(warehouse);

        InventoryRequest request = InventoryTestData.request();
        request.setProductId(product.getId());
        request.setWarehouseId(warehouse.getId());

        //Act
        mockMvc.perform(post("/api/v1/inventories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.productId")
                        .value(product.getId()))

                .andExpect(jsonPath("$.productCode")
                        .value(product.getProductCode()))

                .andExpect(jsonPath("$.warehouseId")
                        .value(warehouse.getId()))

                .andExpect(jsonPath("$.warehouseCode")
                        .value(warehouse.getWarehouseCode()))

                .andExpect(jsonPath("$.warehouseName")
                        .value(warehouse.getName()))

                .andExpect(jsonPath("$.quantity")
                        .value(request.getQuantity()))

                .andExpect(jsonPath("$.reservedQuantity")
                        .value(0))

                .andExpect(jsonPath("$.availableQuantity")
                        .value(request.getQuantity()))

                .andExpect(jsonPath("$.minimumStock")
                        .value(request.getMinimumStock()))

                .andExpect(jsonPath("$.maximumStock")
                        .value(request.getMaximumStock()));

        //Database Verification
        assertEquals(1, inventoryRepository.count());
        Inventory savedInventory = inventoryRepository.findAll().get(0);
        assertEquals(product.getId(), savedInventory.getProduct().getId());
        assertEquals(warehouse.getId(), savedInventory.getWarehouse().getId());

    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldThrowExceptionWhenInventoryAlreadyExists()throws Exception{
        //Arrange
        Category category = CategoryTestData.entity();
        category = categoryRepository.save(category);

        Product product = ProductTestData.entity();
        product.setCategory(category);
        product = productRepository.save(product);
        Warehouse warehouse = WarehouseTestData.entity();
        warehouse = warehouseRepository.save(warehouse);
        Inventory inventory = InventoryTestData.entity();
        inventory.setProduct(product);
        inventory.setWarehouse(warehouse);
        inventory.setInventoryStatus(InventoryStatus.ACTIVE);
        inventoryRepository.save(inventory);
        InventoryRequest request = InventoryTestData.request();
        request.setProductId(product.getId());
        request.setWarehouseId(warehouse.getId());
        //Act+Assert
        mockMvc.perform(post("/api/v1/inventories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Inventory already exists for this product and warehouse"));
        //Assert
        //Data base verification
        assertEquals(1, inventoryRepository.count());

    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnNotFoundWhenProductDoesNotExist()throws Exception{
        //Arrange
        Warehouse warehouse = WarehouseTestData.entity();
        warehouse = warehouseRepository.save(warehouse);
        InventoryRequest request = InventoryTestData.request();
        request.setProductId(99999L);
        request.setWarehouseId(warehouse.getId());
        //Act and assert
        mockMvc.perform(post("/api/v1/inventories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found"));
        //Database verification
        assertEquals(0, inventoryRepository.count());
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnNotFoundWhenWarehouseDoesNotExist() throws Exception {

        // Arrange

        Category category = CategoryTestData.entity();
        category = categoryRepository.save(category);

        Product product = ProductTestData.entity();
        product.setCategory(category);
        product = productRepository.save(product);
        InventoryRequest request = InventoryTestData.request();

        request.setProductId(product.getId());
        request.setWarehouseId(99999L);

        // Act + Assert

        mockMvc.perform(post("/api/v1/inventories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Warehouse not found"));

        // Database verification

        assertEquals(0, inventoryRepository.count());
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnBadRequestWhenMinimumStockIsGreaterThanMaximumStock() throws Exception{
        //Arrange
        Category category = CategoryTestData.entity();
        category = categoryRepository.save(category);

        Product product = ProductTestData.entity();
        product.setCategory(category);
        product = productRepository.save(product);
        Warehouse warehouse = WarehouseTestData.entity();
        warehouse = warehouseRepository.save(warehouse);
        InventoryRequest request = InventoryTestData.request();
        request.setProductId(product.getId());
        request.setWarehouseId(warehouse.getId());
        request.setMinimumStock(100);
        request.setMaximumStock(50);
        //Act and asset
        mockMvc.perform(post("/api/v1/inventories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Minimum stock cannot be greater than maximum stock."));
        //Database Verification
        assertEquals(0, inventoryRepository.count());
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldGetInventoryByIdSuccessfully()throws Exception{
        Category category = CategoryTestData.entity();
        category = categoryRepository.save(category);

        Product product = ProductTestData.entity();
        product.setCategory(category);
        product = productRepository.save(product);
        Warehouse warehouse = WarehouseTestData.entity();
        warehouse = warehouseRepository.save(warehouse);
        Inventory inventory = InventoryTestData.entity();
        inventory.setProduct(product);
        inventory.setWarehouse(warehouse);
        inventory.setInventoryStatus(InventoryStatus.ACTIVE);
        inventory = inventoryRepository.save(inventory);
        //Act and assert
        mockMvc.perform(get("/api/v1/inventories/{id}", inventory.getId()))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.productId")
                        .value(product.getId()))

                .andExpect(jsonPath("$.productCode")
                        .value(product.getProductCode()))

                .andExpect(jsonPath("$.warehouseId")
                        .value(warehouse.getId()))

                .andExpect(jsonPath("$.warehouseCode")
                        .value(warehouse.getWarehouseCode()))

                .andExpect(jsonPath("$.warehouseName")
                        .value(warehouse.getName()))

                .andExpect(jsonPath("$.quantity")
                        .value(inventory.getQuantity()))

                .andExpect(jsonPath("$.reservedQuantity")
                        .value(inventory.getReservedQuantity()))

                .andExpect(jsonPath("$.availableQuantity")
                        .value(inventory.getQuantity()
                                - inventory.getReservedQuantity()))

                .andExpect(jsonPath("$.minimumStock")
                        .value(inventory.getMinimumStock()))

                .andExpect(jsonPath("$.maximumStock")
                        .value(inventory.getMaximumStock()));
        //Database didn't change so we dont do assert

    }
    @Test
    @WithMockUser
    void shouldReturnNotFoundWhenInventoryDoesNotExist() throws Exception{
        //Arrange
        //We are making test fail so we just put below in arrange
        Long inventoryId = 9999L;
       // Act and assert
        mockMvc.perform(get("/api/v1/inventories/{id}", inventoryId))

                .andExpect(status().isNotFound())

                .andExpect(jsonPath("$.message")
                        .value("Inventory not found."));

    }
    @Test
    @WithMockUser
    void shouldGetAllInventoriesSuccessfully()throws Exception{
        //Arrange
        Category category1 = categoryRepository.save(CategoryTestData.entity());

        Product product1 = ProductTestData.entity();
        product1.setCategory(category1);
        product1.setProductCode("PRD-00001");
        product1 = productRepository.save(product1);

        Warehouse warehouse1 = WarehouseTestData.entity();
        warehouse1.setWarehouseCode("WH-00001");
        warehouse1 = warehouseRepository.save(warehouse1);

        Inventory inventory1 = InventoryTestData.entity();
        inventory1.setProduct(product1);
        inventory1.setWarehouse(warehouse1);
        inventory1.setInventoryStatus(InventoryStatus.ACTIVE);

        inventoryRepository.save(inventory1);


        Category category2 = categoryRepository.save(CategoryTestData.entity());

        Product product2 = ProductTestData.entity();
        product2.setCategory(category2);
        product2.setProductCode("PRD-00002");
        product2.setName("Laptop");
        product2 = productRepository.save(product2);
        Warehouse warehouse2 = WarehouseTestData.entity();
        warehouse2.setWarehouseCode("WH-00002");
        warehouse2.setName("Secondary Warehouse");
        warehouse2 = warehouseRepository.save(warehouse2);

        Inventory inventory2 = InventoryTestData.entity();
        inventory2.setProduct(product2);
        inventory2.setWarehouse(warehouse2);
        inventory2.setInventoryStatus(InventoryStatus.ACTIVE);
        inventoryRepository.save(inventory2);
        // Act + Assert

        mockMvc.perform(get("/api/v1/inventories")
                        .param("page", "0")
                        .param("size", "10"))

                .andExpect(status().isOk())
                // Page information
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                // Content size
                .andExpect(jsonPath("$.content.length()").value(2))
                // First inventory
                .andExpect(jsonPath("$.content[0].productId")
                        .value(product1.getId()))
                .andExpect(jsonPath("$.content[0].productCode")
                        .value(product1.getProductCode()))
                .andExpect(jsonPath("$.content[0].warehouseId")
                        .value(warehouse1.getId()))
                .andExpect(jsonPath("$.content[0].warehouseCode")
                        .value(warehouse1.getWarehouseCode()))
                .andExpect(jsonPath("$.content[0].warehouseName")
                        .value(warehouse1.getName()))
                // Second inventory
                .andExpect(jsonPath("$.content[1].productId")
                        .value(product2.getId()))
                .andExpect(jsonPath("$.content[1].productCode")
                        .value(product2.getProductCode()))
                .andExpect(jsonPath("$.content[1].warehouseId")
                        .value(warehouse2.getId()))
                .andExpect(jsonPath("$.content[1].warehouseCode")
                        .value(warehouse2.getWarehouseCode()))
                .andExpect(jsonPath("$.content[1].warehouseName")
                        .value(warehouse2.getName()));

    }
    @Test
    @WithMockUser
    void shouldFilterInventoriesByStatus()throws Exception{
        Category category1 = categoryRepository.save(CategoryTestData.entity());

        Product product1 = ProductTestData.entity();
        product1.setCategory(category1);
        product1 = productRepository.save(product1);
        Warehouse warehouse1 = WarehouseTestData.entity();
        warehouse1 = warehouseRepository.save(warehouse1);

        Inventory inventory1 = InventoryTestData.savedEntity(product1, warehouse1);
        inventory1.setInventoryStatus(InventoryStatus.ACTIVE);
        inventoryRepository.save(inventory1);

        Category category2 = categoryRepository.save(CategoryTestData.entity());

        Product product2 = ProductTestData.entity();
        product2.setCategory(category2);
        product2.setProductCode("PRD-00002");
        product2.setName("Laptop");
        product2 = productRepository.save(product2);

        Warehouse warehouse2 = WarehouseTestData.entity();
        warehouse2.setWarehouseCode("WH-00002");
        warehouse2.setName("Melbourne Warehouse");
        warehouse2.setEmail("melbourne@nexusprocure.com");
        warehouse2 = warehouseRepository.save(warehouse2);

        Inventory inventory2 = InventoryTestData.savedEntity(product2, warehouse2);
        inventory2.setInventoryStatus(InventoryStatus.INACTIVE);
        inventoryRepository.save(inventory2);
        //Act and assert
        mockMvc.perform(get("/api/v1/inventories")
                .param("inventoryStatus", "ACTIVE")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].productId").value(product1.getId()))
                .andExpect(jsonPath("$.content[0].warehouseId").value(warehouse1.getId()))
                .andExpect(jsonPath("$.content[0].quantity").value(inventory1.getQuantity()));

    }
    @Test
    @WithMockUser
    void shouldFilterInventoriesByProduct()throws Exception{
        //Arrange
        Category category1 = CategoryTestData.entity();
        category1 = categoryRepository.save(category1);

        Product product1 = ProductTestData.entity();
        product1.setCategory(category1);
        product1 = productRepository.save(product1);

        Warehouse warehouse1 = WarehouseTestData.entity();
        warehouse1 = warehouseRepository.save(warehouse1);

        Inventory inventory1 = InventoryTestData.savedEntity(product1, warehouse1);
        inventoryRepository.save(inventory1);
        Category category2 = CategoryTestData.entity();
        category2.setName("Computers_" + System.nanoTime());
        category2 = categoryRepository.save(category2);

        Product product2 = ProductTestData.entity();
        product2.setCategory(category2);
        product2.setProductCode("PRD-00002");
        product2.setName("Laptop");
        product2 = productRepository.save(product2);

        Warehouse warehouse2 = WarehouseTestData.entity();
        warehouse2.setWarehouseCode("WH-00002");
        warehouse2.setName("Melbourne Warehouse");
        warehouse2.setEmail("melbourne@nexusprocure.com");
        warehouse2 = warehouseRepository.save(warehouse2);
        Inventory inventory2 = InventoryTestData.savedEntity(product2, warehouse2);
        inventoryRepository.save(inventory2);
        mockMvc.perform(get("/api/v1/inventories")
                .param("productId", product1.getId().toString())
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].productId")
                        .value(product1.getId()))
                .andExpect(jsonPath("$.content[0].warehouseId")
                        .value(warehouse1.getId()));
    }
    @Test
    @WithMockUser
    void shouldFilterInventoriesByWarehouse() throws Exception {

        // Arrange

        Category category1 = CategoryTestData.entity();
        category1 = categoryRepository.save(category1);

        Product product1 = ProductTestData.entity();
        product1.setCategory(category1);
        product1 = productRepository.save(product1);

        Warehouse warehouse1 = WarehouseTestData.entity();
        warehouse1 = warehouseRepository.save(warehouse1);

        Inventory inventory1 = InventoryTestData.savedEntity(product1, warehouse1);
        inventoryRepository.save(inventory1);

        Category category2 = CategoryTestData.entity();
        category2.setName("Computers_" + System.nanoTime());
        category2 = categoryRepository.save(category2);

        Product product2 = ProductTestData.entity();
        product2.setCategory(category2);
        product2.setProductCode("PRD-00002");
        product2.setName("Laptop");
        product2 = productRepository.save(product2);

        Warehouse warehouse2 = WarehouseTestData.entity();
        warehouse2.setWarehouseCode("WH-00002");
        warehouse2.setName("Melbourne Warehouse");
        warehouse2.setEmail("melbourne@nexusprocure.com");
        warehouse2 = warehouseRepository.save(warehouse2);

        Inventory inventory2 = InventoryTestData.savedEntity(product2, warehouse2);
        inventoryRepository.save(inventory2);

        // Act + Assert

        mockMvc.perform(get("/api/v1/inventories")
                        .param("warehouseId", warehouse1.getId().toString())
                        .param("page", "0")
                        .param("size", "10"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].warehouseId")
                        .value(warehouse1.getId()))
                .andExpect(jsonPath("$.content[0].productId")
                        .value(product1.getId()));
    }
    @Test
    @WithMockUser
    void shouldFilterInventoriesByQuantityRange() throws Exception {

        // Arrange
        Category category1 = CategoryTestData.entity();
        category1 = categoryRepository.save(category1);

        Product product1 = ProductTestData.entity();
        product1.setCategory(category1);
        product1 = productRepository.save(product1);

        Warehouse warehouse1 = WarehouseTestData.entity();
        warehouse1 = warehouseRepository.save(warehouse1);

        Inventory inventory1 = InventoryTestData.savedEntity(product1, warehouse1);
        inventory1.setQuantity(50);
        inventoryRepository.save(inventory1);

        Category category2 = CategoryTestData.entity();
        category2.setName("Computers_" + System.nanoTime());
        category2 = categoryRepository.save(category2);

        Product product2 = ProductTestData.entity();
        product2.setCategory(category2);
        product2.setProductCode("PRD-00002");
        product2.setName("Laptop");
        product2 = productRepository.save(product2);

        Warehouse warehouse2 = WarehouseTestData.entity();
        warehouse2.setWarehouseCode("WH-00002");
        warehouse2.setName("Melbourne Warehouse");
        warehouse2.setEmail("melbourne@nexusprocure.com");
        warehouse2 = warehouseRepository.save(warehouse2);

        Inventory inventory2 = InventoryTestData.savedEntity(product2, warehouse2);
        inventory2.setQuantity(150);
        inventoryRepository.save(inventory2);

        // Act + Assert

        mockMvc.perform(get("/api/v1/inventories")
                        .param("minimumQuantity", "40")
                        .param("maximumQuantity", "100")
                        .param("page", "0")
                        .param("size", "10"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].quantity")
                        .value(50))
                .andExpect(jsonPath("$.content[0].productId")
                        .value(product1.getId()));
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldUpdateInventorySuccessfully() throws Exception {

        // Arrange

        Category category = CategoryTestData.entity();
        category = categoryRepository.save(category);

        Product product = ProductTestData.entity();
        product.setCategory(category);
        product = productRepository.save(product);

        Warehouse warehouse = WarehouseTestData.entity();
        warehouse = warehouseRepository.save(warehouse);

        Inventory inventory = InventoryTestData.savedEntity(product, warehouse);
        inventory = inventoryRepository.save(inventory);

        InventoryUpdateRequest request = InventoryTestData.updateRequest();
        request.setMinimumStock(20);
        request.setMaximumStock(200);

        // Act + Assert

        mockMvc.perform(put("/api/v1/inventories/{id}", inventory.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.minimumStock").value(20))
                .andExpect(jsonPath("$.maximumStock").value(200));

        // Database verification

        Inventory updatedInventory = inventoryRepository
                .findById(inventory.getId())
                .orElseThrow();

        assertEquals(20, updatedInventory.getMinimumStock());
        assertEquals(200, updatedInventory.getMaximumStock());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnNotFoundWhenUpdatingNonexistingInventory()throws Exception{
        InventoryUpdateRequest request = InventoryTestData.updateRequest();
        request.setMinimumStock(20);
        request.setMaximumStock(200);
        mockMvc.perform(put("/api/v1/inventories/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnBadRequestWhenUpdatingWithInvalidStockLevels() throws Exception {

        // Arrange

        Category category = CategoryTestData.entity();
        category = categoryRepository.save(category);

        Product product = ProductTestData.entity();
        product.setCategory(category);
        product = productRepository.save(product);

        Warehouse warehouse = WarehouseTestData.entity();
        warehouse = warehouseRepository.save(warehouse);

        Inventory inventory = InventoryTestData.savedEntity(product, warehouse);
        inventory = inventoryRepository.save(inventory);

        InventoryUpdateRequest request = InventoryTestData.updateRequest();
        request.setMinimumStock(200);
        request.setMaximumStock(20);

        // Act + Assert

        mockMvc.perform(put("/api/v1/inventories/{id}", inventory.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isBadRequest());
        Inventory unchangedInventory = inventoryRepository
                .findById(inventory.getId())
                .orElseThrow();
        assertEquals(10, unchangedInventory.getMinimumStock());
        assertEquals(100, unchangedInventory.getMaximumStock());
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldActivateInventorySuccessfully() throws Exception {
        // Arrange

        Category category = CategoryTestData.entity();
        category = categoryRepository.save(category);

        Product product = ProductTestData.entity();
        product.setCategory(category);
        product = productRepository.save(product);
        Warehouse warehouse = WarehouseTestData.entity();
        warehouse = warehouseRepository.save(warehouse);

        Inventory inventory = InventoryTestData.savedEntity(product, warehouse);
        inventory.setInventoryStatus(InventoryStatus.INACTIVE);
        inventory = inventoryRepository.save(inventory);

        // Act

        mockMvc.perform(put("/api/v1/inventories/{id}/activate",
                        inventory.getId()))

                .andExpect(status().isNoContent());

        // Database verification

        Inventory activatedInventory = inventoryRepository
                .findById(inventory.getId())
                .orElseThrow();

        assertEquals(
                InventoryStatus.ACTIVE,
                activatedInventory.getInventoryStatus());
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldDeactivateInventorySuccessfully() throws Exception {

        // Arrange

        Category category = CategoryTestData.entity();
        category = categoryRepository.save(category);

        Product product = ProductTestData.entity();
        product.setCategory(category);
        product = productRepository.save(product);

        Warehouse warehouse = WarehouseTestData.entity();
        warehouse = warehouseRepository.save(warehouse);

        Inventory inventory = InventoryTestData.savedEntity(product, warehouse);
        inventory.setInventoryStatus(InventoryStatus.ACTIVE);
        inventory = inventoryRepository.save(inventory);

        // Act

        mockMvc.perform(put("/api/v1/inventories/{id}/deactivate",
                        inventory.getId()))

                .andExpect(status().isNoContent());

        // Database verification

        Inventory deactivatedInventory = inventoryRepository
                .findById(inventory.getId())
                .orElseThrow();

        assertEquals(
                InventoryStatus.INACTIVE,
                deactivatedInventory.getInventoryStatus());
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnNotFoundWhenActivatingNonExistingInventory() throws Exception {

        // Act + Assert

        mockMvc.perform(
                        put("/api/v1/inventories/{id}/activate", 999L))

                .andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnNotFoundWhenDeactivatingNonExistingInventory() throws Exception {

        // Act + Assert

        mockMvc.perform(
                        put("/api/v1/inventories/{id}/deactivate", 999L))

                .andExpect(status().isNotFound());
    }


}
