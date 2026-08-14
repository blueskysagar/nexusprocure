package com.nexusprocure.inventory.service;

import com.nexusprocure.common.Response.PageResponse;
import com.nexusprocure.exception.DuplicateResourceException;
import com.nexusprocure.exception.InvalidInventoryConfigurationException;
import com.nexusprocure.exception.ResourceNotFoundException;
import com.nexusprocure.inventory.dto.request.InventoryFilterRequest;
import com.nexusprocure.inventory.dto.request.InventoryRequest;
import com.nexusprocure.inventory.dto.response.InventoryResponse;
import com.nexusprocure.inventory.dto.update.InventoryUpdateRequest;
import com.nexusprocure.inventory.entity.Inventory;
import com.nexusprocure.inventory.enums.InventoryStatus;
import com.nexusprocure.inventory.mapper.InventoryMapper;
import com.nexusprocure.inventory.repository.InventoryRepository;
import com.nexusprocure.inventory.service.Impl.InventoryServiceImpl;
import com.nexusprocure.inventory.testdata.InventoryTestData;
import com.nexusprocure.product.entity.Product;
import com.nexusprocure.product.repository.ProductRepository;
import com.nexusprocure.product.testdata.ProductTestData;
import com.nexusprocure.warehouse.entity.Warehouse;
import com.nexusprocure.warehouse.repository.WarehouseRepository;
import com.nexusprocure.warehouse.testdata.WarehouseTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceImplTest {
    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private WarehouseRepository warehouseRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private InventoryMapper inventoryMapper;
    @InjectMocks
    private InventoryServiceImpl inventoryService;
    @Test
    void shouldCreateInventorySuccessfully(){
        //Arrange
        InventoryRequest request = InventoryTestData.request();
        Product product = ProductTestData.entityWithId();
        Warehouse warehouse = WarehouseTestData.entityWithId();
        Inventory newInventory = InventoryTestData.entity();
        Inventory savedInventory = InventoryTestData.savedEntity(product, warehouse);
        InventoryResponse expectedResponse = InventoryTestData.response();
        // Stub(when this collaborator is called simply return this object)
        when(productRepository.findById(request.getProductId()))
                .thenReturn(Optional.of(product));
        when(warehouseRepository.findById(request.getWarehouseId()))
                .thenReturn(Optional.of(warehouse));
         when(inventoryRepository.findByProduct_IdAndWarehouse_Id(request.getProductId(), request.getWarehouseId()))
                 .thenReturn(Optional.empty());
         when(inventoryMapper.toEntity(request))
                 .thenReturn(newInventory);
         when(inventoryRepository.save(any(Inventory.class)))
                 .thenReturn(savedInventory);
         when(inventoryMapper.toResponse(savedInventory))
                 .thenReturn(expectedResponse);
         //Act(Just call the method you are calling in this case, create Inventory)
        InventoryResponse actualResponse = inventoryService.createInventory(request);
        //Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getProductId(),
                actualResponse.getProductId());

        assertEquals(expectedResponse.getProductCode(),
                actualResponse.getProductCode());

        assertEquals(expectedResponse.getWarehouseId(),
                actualResponse.getWarehouseId());

        assertEquals(expectedResponse.getWarehouseCode(),
                actualResponse.getWarehouseCode());

        assertEquals(expectedResponse.getWarehouseName(),
                actualResponse.getWarehouseName());

        assertEquals(expectedResponse.getQuantity(),
                actualResponse.getQuantity());

        assertEquals(expectedResponse.getReservedQuantity(),
                actualResponse.getReservedQuantity());

        assertEquals(expectedResponse.getAvailableQuantity(),
                actualResponse.getAvailableQuantity());

        assertEquals(expectedResponse.getMinimumStock(),
                actualResponse.getMinimumStock());

        assertEquals(expectedResponse.getMaximumStock(),
                actualResponse.getMaximumStock());
        //Verify(Did my service call the collaborator)
        verify(productRepository,times(1)).findById(request.getProductId());
        verify(warehouseRepository,times(1)).findById(request.getWarehouseId());
        verify(inventoryRepository,times(1)).findByProduct_IdAndWarehouse_Id(request.getProductId(), request.getWarehouseId());
        verify(inventoryMapper,times(1)).toEntity(request);
        verify(inventoryMapper,times(1)).toResponse(savedInventory);
        ArgumentCaptor<Inventory> inventoryCaptor = ArgumentCaptor.forClass(Inventory.class);
        verify(inventoryRepository, times(1)).save(inventoryCaptor.capture());
        verify(inventoryRepository, times(1))
                .save(inventoryCaptor.capture());
        Inventory capturedInventory = inventoryCaptor.getValue();
        assertEquals(product, capturedInventory.getProduct());
        assertEquals(warehouse, capturedInventory.getWarehouse());
        assertEquals(request.getQuantity(), capturedInventory.getQuantity());
        assertEquals(request.getMinimumStock(), capturedInventory.getMinimumStock());
        assertEquals(request.getMaximumStock(), capturedInventory.getMaximumStock());
        assertEquals(0, capturedInventory.getReservedQuantity());
        assertEquals(InventoryStatus.ACTIVE, capturedInventory.getInventoryStatus());

        verifyNoMoreInteractions(
                inventoryRepository,
                productRepository,
                warehouseRepository,
                inventoryMapper
        );

    }
    @Test
    void shouldThrowResourceNotFoundWhenProductDoesNotExist(){
        //Arrange
        InventoryRequest request = InventoryTestData.request();
        when(productRepository.findById(request.getProductId()))
                .thenReturn(Optional.empty());
        //Act andAssert
        assertThatThrownBy(() -> inventoryService.createInventory(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product");
        //Verify
        verify(productRepository).findById(request.getProductId());
        verifyNoInteractions(warehouseRepository);
        verifyNoInteractions(inventoryRepository);
        verifyNoInteractions(inventoryMapper);

    }
    @Test
    void shouldThrowResourceNotFoundWhenWarehouseDoesNotExist(){
        //Arrange
        InventoryRequest request = InventoryTestData.request();
        Product product = ProductTestData.entity();
        when(productRepository.findById(request.getProductId()))
                .thenReturn(Optional.of(product));
        when(warehouseRepository.findById(request.getWarehouseId()))
                .thenReturn(Optional.empty());
        //Act and assert
        assertThatThrownBy(() -> inventoryService.createInventory(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Warehouse");
        //Verify
        verify(productRepository).findById(request.getProductId());
        verify(warehouseRepository).findById(request.getWarehouseId());
        verifyNoInteractions(inventoryRepository);
        verifyNoInteractions(inventoryMapper);

    }
    @Test
    void shouldThrowDuplicateResourceExceptionWhenInventoryAlreadyExists(){
        InventoryRequest request = InventoryTestData.request();
        Product product = ProductTestData.entityWithId();
        Warehouse warehouse = WarehouseTestData.entityWithId();
        Inventory existingInventory =
                InventoryTestData.savedEntity(product, warehouse);
        when(productRepository.findById(request.getProductId()))
                .thenReturn(Optional.of(product));

        when(warehouseRepository.findById(request.getWarehouseId()))
                .thenReturn(Optional.of(warehouse));
        when(inventoryRepository.findByProduct_IdAndWarehouse_Id(request.getProductId(), request.getWarehouseId()))
                .thenReturn(Optional.of(existingInventory));
        //Act and assert
        assertThatThrownBy(() -> inventoryService.createInventory(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Inventory already exists");
        //Verify
        verify(productRepository).findById(request.getProductId());

        verify(warehouseRepository).findById(request.getWarehouseId());

        verify(inventoryRepository).findByProduct_IdAndWarehouse_Id(
                request.getProductId(),
                request.getWarehouseId());

        verifyNoInteractions(inventoryMapper);
        verify(inventoryRepository,never()).save(any(Inventory.class));
    }
    @Test
    void shouldThrowInvalidInventoryConfigurationWhenMinimumStockIsGreaterThanMaximumStock(){
        InventoryRequest request = InventoryTestData.request();
        request.setMinimumStock(100);
        request.setMaximumStock(50);

        Product product = ProductTestData.entityWithId();
        Warehouse warehouse = WarehouseTestData.entityWithId();

        when(productRepository.findById(request.getProductId()))
                .thenReturn(Optional.of(product));

        when(warehouseRepository.findById(request.getWarehouseId()))
                .thenReturn(Optional.of(warehouse));

        when(inventoryRepository.findByProduct_IdAndWarehouse_Id(
                request.getProductId(),
                request.getWarehouseId()))
                .thenReturn(Optional.empty());
        // Act & Assert
        assertThatThrownBy(() -> inventoryService.createInventory(request))
                .isInstanceOf(InvalidInventoryConfigurationException.class)
                .hasMessageContaining("Minimum stock cannot be greater than maximum stock.");

        // Verify
        verify(productRepository).findById(request.getProductId());
        verify(warehouseRepository).findById(request.getWarehouseId());

        verify(inventoryRepository)
                .findByProduct_IdAndWarehouse_Id(
                        request.getProductId(),
                        request.getWarehouseId());

        verifyNoInteractions(inventoryMapper);
        verify(inventoryRepository, never()).save(any(Inventory.class));
    }
    @Test
    void shouldGetInventoryByIdSuccessfully(){
        //Arrange
        Inventory inventory = InventoryTestData.entityWithId();
        InventoryResponse expectedResponse = InventoryTestData.response();
        when(inventoryRepository.findById(inventory.getId()))
                .thenReturn(Optional.of(inventory));

        when(inventoryMapper.toResponse(inventory))
                .thenReturn(expectedResponse);
        //Act
        InventoryResponse actualResponse =   inventoryService.getInventoryById(inventory.getId());
        //Assert
        assertThat(actualResponse).isEqualTo(expectedResponse);
        //Verify
        verify(inventoryRepository).findById(inventory.getId());
        verify(inventoryMapper).toResponse(inventory);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(warehouseRepository);
    }
    @Test
    void shouldThrowResourceNotFoundWhenInventoryNotFound(){
        // Arrange
        Inventory inventory = InventoryTestData.entityWithId();

        when(inventoryRepository.findById(inventory.getId()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> inventoryService.getInventoryById(inventory.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Inventory not found.");

        // Verify
        verify(inventoryRepository).findById(inventory.getId());

        verifyNoInteractions(inventoryMapper);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(warehouseRepository);
    }
    @Test
    void shouldReturnPagedInventoriesSuccessfully(){
        InventoryFilterRequest filter = InventoryTestData.emptyFilter();
        Pageable pageable = PageRequest.of(0,10);
        Inventory inventory = InventoryTestData.entity();
        Page<Inventory> inventoryPage = new PageImpl<>(List.of(inventory), pageable, 1);
        InventoryResponse response = InventoryTestData.response();
        when(inventoryRepository.findAll(any(Specification.class),eq(pageable)))
                .thenReturn(inventoryPage);
        when(inventoryMapper.toResponse(inventory))
                .thenReturn(response);
        //Act
        PageResponse<InventoryResponse> actualResponse = inventoryService.getAllInventories(filter, pageable);
        //Assert
        assertThat(actualResponse.getContent()).hasSize(1);
        assertThat(actualResponse.getPage()).isEqualTo(0);

        assertThat(actualResponse.getSize()).isEqualTo(10);
        assertThat(actualResponse.getTotalElements()).isEqualTo(1);
        assertThat(actualResponse.getTotalPages()).isEqualTo(1);

        // Verify
        verify(inventoryRepository)
                .findAll(any(Specification.class), eq(pageable));

        verify(inventoryMapper).toResponse(inventory);

        verifyNoInteractions(productRepository);
        verifyNoInteractions(warehouseRepository);
    }
    @Test
    void shouldReturnEmptyPageWhenNoInventoriesFound(){
        //Arrange
        InventoryFilterRequest filter = InventoryTestData.emptyFilter();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Inventory> inventoryPage = Page.empty(pageable);
        when(inventoryRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(inventoryPage);
        // Act
        PageResponse<InventoryResponse> actualResponse =
                inventoryService.getAllInventories(filter, pageable);
        assertThat(actualResponse.getContent()).isEmpty();
        assertThat(actualResponse.getTotalElements()).isZero();
        assertThat(actualResponse.getTotalPages()).isZero();
        assertThat(actualResponse.getPage()).isZero();
        assertThat(actualResponse.getSize()).isEqualTo(10);
        // Verify
        verify(inventoryRepository)
                .findAll(any(Specification.class), eq(pageable));

        verifyNoInteractions(inventoryMapper);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(warehouseRepository);
    }
    @Test
    void shouldUpdateInventorySuccessfully(){
        //Arrange
        Inventory inventory = InventoryTestData.entityWithId();
        InventoryUpdateRequest request =
                InventoryTestData.updateRequest();
        request.setMinimumStock(20);
        request.setMaximumStock(200);
        InventoryResponse expectedResponse =
                InventoryTestData.response();
        expectedResponse.setMinimumStock(20);
        expectedResponse.setMaximumStock(200);
        when(inventoryRepository.findById(inventory.getId()))
                .thenReturn(Optional.of(inventory));

        when(inventoryMapper.toResponse(inventory))
                .thenReturn(expectedResponse);
        // Act
        InventoryResponse actualResponse =
                inventoryService.updateInventory(
                        inventory.getId(),
                        request);
        //Assert
        assertThat(inventory.getMinimumStock()).isEqualTo(20);
        assertThat(inventory.getMaximumStock()).isEqualTo(200);
        assertThat(actualResponse).isSameAs(expectedResponse);
        // Verify
        verify(inventoryRepository).findById(inventory.getId());

        verify(inventoryMapper).toResponse(inventory);

        verifyNoInteractions(productRepository);
        verifyNoInteractions(warehouseRepository);

        verify(inventoryRepository, never()).save(any());

    }
    @Test
    void shouldThrowResourceNotFoundWhenUpdatingInventoryNotFound(){
        Inventory inventory = InventoryTestData.entityWithId();
        InventoryUpdateRequest request = InventoryTestData.updateRequest();

        when(inventoryRepository.findById(inventory.getId()))
                .thenReturn(Optional.empty());
        //Act and Assert
        assertThatThrownBy(() -> inventoryService.updateInventory(inventory.getId(), request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Inventory not found");
        verify(inventoryRepository).findById(inventory.getId());

        verifyNoInteractions(inventoryMapper);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(warehouseRepository);
    }
    @Test
    void shouldThrowInvalidInventoryConfigurationWhenUpdating(){
        //Arrange
        Inventory inventory = InventoryTestData.entityWithId();
        // For Update only if we have to throw exception of this,
        // we set originalMinimum and original Maximum, we include only in this case
        //if the same inventory state is getting set and changed
        Integer originalMinimum = inventory.getMinimumStock();
        Integer originalMaximum = inventory.getMaximumStock();
        InventoryUpdateRequest request = InventoryTestData.updateRequest();
        request.setMinimumStock(200);
        request.setMaximumStock(100);
        when(inventoryRepository.findById(inventory.getId()))
                .thenReturn(Optional.of(inventory));
        //Act and assert
        assertThatThrownBy(() -> inventoryService.updateInventory(inventory.getId(),request))
                .isInstanceOf(InvalidInventoryConfigurationException.class)
                .hasMessageContaining("Minimum stock cannot be greater than maximum stock");
        //if validation failed than new state and old state should be same
        assertThat(inventory.getMinimumStock()).isEqualTo(originalMinimum);

        assertThat(inventory.getMaximumStock())
                .isEqualTo(originalMaximum);
        // Verify
        verify(inventoryRepository).findById(inventory.getId());

        verifyNoInteractions(inventoryMapper);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(warehouseRepository);
    }
    @Test
    void shouldActivateInventorySuccessfully(){
        //Arrange
        Inventory inventory = InventoryTestData.entityWithId();
        inventory.setInventoryStatus(InventoryStatus.INACTIVE);
        when(inventoryRepository.findById(inventory.getId()))
                .thenReturn(Optional.of(inventory));
        //Act
        inventoryService.activateInventory(inventory.getId());
        //Assert
        assertThat(inventory.getInventoryStatus()).isEqualTo(InventoryStatus.ACTIVE);
        //Verify
        verify(inventoryRepository).findById(inventory.getId());

        verifyNoInteractions(inventoryMapper);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(warehouseRepository);

    }
    @Test
    void shouldThrowResourceNotFoundWhenActivatingInventory(){
        //Arrange
        Inventory inventory = InventoryTestData.entityWithId();

        when(inventoryRepository.findById(inventory.getId()))
                .thenReturn(Optional.empty());
        //Act and Assert
        assertThatThrownBy(() ->
                inventoryService.activateInventory(inventory.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Inventory not found.");
        //Verify

        // Verify
        verify(inventoryRepository).findById(inventory.getId());

        verifyNoInteractions(inventoryMapper);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(warehouseRepository);

    }
    @Test
    void shouldDeactivateInventorySuccessfully(){
// Arrange
        Inventory inventory = InventoryTestData.entityWithId();
        //Below we Dont set anything because, Before execution (Arrange): ACTIVE ✅
        //After execution (Act): INACTIVE ✅

        when(inventoryRepository.findById(inventory.getId()))
                .thenReturn(Optional.of(inventory));

        // Act
        inventoryService.deactivateInventory(inventory.getId());

        // Assert
        assertThat(inventory.getInventoryStatus())
                .isEqualTo(InventoryStatus.INACTIVE);

        // Verify
        verify(inventoryRepository).findById(inventory.getId());

        verifyNoInteractions(inventoryMapper);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(warehouseRepository);

    }
    @Test
    void shouldReturnResourceNotFoundWhenDeactivatingInventory(){
        //Arrange
        Inventory inventory = InventoryTestData.entityWithId();

        when(inventoryRepository.findById(inventory.getId()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() ->
                inventoryService.deactivateInventory(inventory.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Inventory not found.");
        // Verify
        verify(inventoryRepository).findById(inventory.getId());

        verifyNoInteractions(inventoryMapper);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(warehouseRepository);
    }



}
