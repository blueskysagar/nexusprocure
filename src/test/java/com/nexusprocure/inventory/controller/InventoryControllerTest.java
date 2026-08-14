
package com.nexusprocure.inventory.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexusprocure.authentication.Service.CustomUserDetailsService;
import com.nexusprocure.authentication.Service.JwtService;
import com.nexusprocure.common.Response.PageResponse;
import com.nexusprocure.config.*;
import com.nexusprocure.exception.DuplicateResourceException;
import com.nexusprocure.exception.GlobalExceptionalHandler;
import com.nexusprocure.exception.ResourceNotFoundException;
import com.nexusprocure.inventory.dto.request.InventoryFilterRequest;
import com.nexusprocure.inventory.dto.request.InventoryRequest;
import com.nexusprocure.inventory.dto.response.InventoryResponse;
import com.nexusprocure.inventory.dto.update.InventoryUpdateRequest;
import com.nexusprocure.inventory.service.InventoryService;
import com.nexusprocure.inventory.testdata.InventoryTestData;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import org.springframework.context.annotation.Import;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
@WebMvcTest(InventoryController.class)
@Import({
        TestSecurityConfig.class,
        GlobalExceptionalHandler.class,
        CustomAuthenticationEntryPoint.class,
        CustomAccessDeniedHandler.class
})
@AutoConfigureMockMvc(addFilters = true)
public class InventoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private InventoryService inventoryService;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldCreateInventorySuccessfully() throws Exception {

        //Arrange
        InventoryRequest request = InventoryTestData.request();
        InventoryResponse response = InventoryTestData.response();
        when(inventoryService.createInventory(any(InventoryRequest.class)))
                .thenReturn(response);
        //act and assert
        mockMvc.perform(post("/api/v1/inventories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))


                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(response.getProductId()))
                .andExpect(jsonPath("$.productCode").value(response.getProductCode()))
                .andExpect(jsonPath("$.warehouseId").value(response.getWarehouseId()))
                .andExpect(jsonPath("$.warehouseCode").value(response.getWarehouseCode()))
                .andExpect(jsonPath("$.warehouseName").value(response.getWarehouseName()))
                .andExpect(jsonPath("$.quantity").value(response.getQuantity()))
                .andExpect(jsonPath("$.reservedQuantity").value(response.getReservedQuantity()))
                .andExpect(jsonPath("$.availableQuantity").value(response.getAvailableQuantity()))
                .andExpect(jsonPath("$.minimumStock").value(response.getMinimumStock()))
                .andExpect(jsonPath("$.maximumStock").value(response.getMaximumStock()));

        //verify
        verify(inventoryService).createInventory(any(InventoryRequest.class));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnBadRequestWhenRequestIsInvalid() throws Exception {
        InventoryRequest request = InventoryTestData.request();
        request.setProductId(null);
        mockMvc.perform(post("/api/v1/inventories")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
        // Verify
        verifyNoInteractions(inventoryService);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldTranslateResourceNotFoundExceptionToNotFoundResponse() throws Exception {
        InventoryRequest request = InventoryTestData.request();

        when(inventoryService.createInventory(any(InventoryRequest.class)))
                .thenThrow (new ResourceNotFoundException("Product not found"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/inventories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        // Verify
        verify(inventoryService).createInventory(any(InventoryRequest.class));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldTranslateDuplicateResourceExceptionToConflictResponse() throws Exception {
        InventoryRequest request = InventoryTestData.request();

        when(inventoryService.createInventory(any(InventoryRequest.class)))
                .thenThrow(new DuplicateResourceException(
                        "Inventory already exists"
                ));
        // Act & Assert
        mockMvc.perform(post("/api/v1/inventories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        // Verify
        verify(inventoryService)
                .createInventory(any(InventoryRequest.class));
    }

    @Test

    void shouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {
        // Arrange
        InventoryRequest request = InventoryTestData.request();

        // Act & Assert
        mockMvc.perform(post("/api/v1/inventories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        // Verify
        verifyNoInteractions(inventoryService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void ShouldReturnForbiddenWhenUserDoesNotHaveRequiredRole() throws Exception {
        // Arrange
        InventoryRequest request = InventoryTestData.request();

        // Act & Assert
        mockMvc.perform(post("/api/v1/inventories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        // Verify
        verifyNoInteractions(inventoryService);

    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnInventoryWhenInventoryExists() throws Exception {
        // Arrange
        Long inventoryId = 1L;

        InventoryResponse response = InventoryTestData.response();

        when(inventoryService.getInventoryById(inventoryId))
                .thenReturn(response);


        // Act & Assert
        mockMvc.perform(get("/api/v1/inventories/{id}", inventoryId))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(response.getProductId()))
                .andExpect(jsonPath("$.productCode").value(response.getProductCode()))
                .andExpect(jsonPath("$.warehouseId").value(response.getWarehouseId()))
                .andExpect(jsonPath("$.warehouseCode").value(response.getWarehouseCode()))
                .andExpect(jsonPath("$.warehouseName").value(response.getWarehouseName()))
                .andExpect(jsonPath("$.quantity").value(response.getQuantity()))
                .andExpect(jsonPath("$.reservedQuantity").value(response.getReservedQuantity()))
                .andExpect(jsonPath("$.availableQuantity").value(response.getAvailableQuantity()))
                .andExpect(jsonPath("$.minimumStock").value(response.getMinimumStock()))
                .andExpect(jsonPath("$.maximumStock").value(response.getMaximumStock()));


        // Verify
        verify(inventoryService)
                .getInventoryById(inventoryId);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnNotFoundWhenInventoryDoesNotExist() throws Exception {

        // Arrange
        Long inventoryId = InventoryTestData.entityWithId().getId();

        when(inventoryService.getInventoryById(inventoryId))
                .thenThrow(new ResourceNotFoundException(
                        "Inventory not found"
                ));


        // Act & Assert
        mockMvc.perform(get("/api/v1/inventories/{id}", inventoryId))
                .andExpect(status().isNotFound());


        // Verify
        verify(inventoryService)
                .getInventoryById(inventoryId);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnPagedInventoriesSuccessfully() throws Exception {
        PageResponse<InventoryResponse> response = InventoryTestData.pageResponse();
        when(inventoryService.getAllInventories(any(InventoryFilterRequest.class), any(Pageable.class)))
                .thenReturn(response);
        mockMvc.perform(get("/api/v1/inventories")
                        .param("page", "0")
                        .param("size", "10")

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));
        // Verify
        verify(inventoryService)
                .getAllInventories(
                        any(InventoryFilterRequest.class),
                        any(Pageable.class)
                );

    }
    @Test
    void ShouldReturnUnauthorizedWhenGettingInventoriesWithoutAuthentication()throws Exception{
        //No arrange because request never reaches to controller
        // Act & Assert
        mockMvc.perform(get("/api/v1/inventories")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isUnauthorized());

        // Verify
        verifyNoInteractions(inventoryService);
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldUpdateInventorySuccessfully() throws Exception{
        // Arrange
        Long inventoryId = InventoryTestData.entityWithId().getId();

        InventoryUpdateRequest request =
                InventoryTestData.updateRequest();

        InventoryResponse response =
                InventoryTestData.response();

        when(inventoryService.updateInventory(
                eq(inventoryId),
                any(InventoryUpdateRequest.class)
        ))
                .thenReturn(response);
        // Act & Assert
        mockMvc.perform(put("/api/v1/inventories/{id}", inventoryId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(response.getProductId()))
                .andExpect(jsonPath("$.productCode").value(response.getProductCode()))
                .andExpect(jsonPath("$.warehouseId").value(response.getWarehouseId()))
                .andExpect(jsonPath("$.warehouseCode").value(response.getWarehouseCode()))
                .andExpect(jsonPath("$.warehouseName").value(response.getWarehouseName()))
                .andExpect(jsonPath("$.quantity").value(response.getQuantity()))
                .andExpect(jsonPath("$.reservedQuantity").value(response.getReservedQuantity()))
                .andExpect(jsonPath("$.availableQuantity").value(response.getAvailableQuantity()))
                .andExpect(jsonPath("$.minimumStock").value(response.getMinimumStock()))
                .andExpect(jsonPath("$.maximumStock").value(response.getMaximumStock()));
        // Verify
        verify(inventoryService)
                .updateInventory(
                        eq(inventoryId),
                        any(InventoryUpdateRequest.class)
                );
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnBadRequestWhenUpdateRequestIsInvalid()throws Exception
    {
        // Arrange
        Long inventoryId = InventoryTestData.entityWithId().getId();

        InventoryUpdateRequest request = InventoryTestData.updateRequest();
        request.setMinimumStock(null);

        // Act & Assert
        mockMvc.perform(put("/api/v1/inventories/{id}", inventoryId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // Verify
        verifyNoInteractions(inventoryService);

    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnNotFoundWhenUpdatingNonExistingInventory() throws Exception{
        Long inventoryId = InventoryTestData.entityWithId().getId();

        InventoryUpdateRequest request = InventoryTestData.updateRequest();

        when(inventoryService.updateInventory(
                eq(inventoryId),
                any(InventoryUpdateRequest.class)
        ))
                .thenThrow(
                        new ResourceNotFoundException("Inventory not found")
                );
        // Act & Assert
        mockMvc.perform(put("/api/v1/inventories/{id}", inventoryId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        // Verify
        verify(inventoryService)
                .updateInventory(
                        eq(inventoryId),
                        any(InventoryUpdateRequest.class)
                );

    }
    @Test
    void shouldReturnUnauthorizedWhenUpdatingWithoutAuthentication() throws Exception {

        // Arrange
        Long inventoryId = InventoryTestData.entityWithId().getId();

        InventoryUpdateRequest request =
                InventoryTestData.updateRequest();

        // Act & Assert
        mockMvc.perform(put("/api/v1/inventories/{id}", inventoryId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        // Verify
        verifyNoInteractions(inventoryService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenWhenUserDoesNotHavePermissionToUpdateInventory() throws Exception {

        // Arrange
        Long inventoryId = InventoryTestData.entityWithId().getId();

        InventoryUpdateRequest request =
                InventoryTestData.updateRequest();

        // Act & Assert
        mockMvc.perform(put("/api/v1/inventories/{id}", inventoryId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        // Verify
        verifyNoInteractions(inventoryService);
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldActivateInventorySuccessfully() throws Exception {

        // Arrange
        Long inventoryId = InventoryTestData.entityWithId().getId();

        doNothing()
                .when(inventoryService)
                .activateInventory(inventoryId);


        // Act + Assert
        mockMvc.perform(
                        put("/api/v1/inventories/{id}/activate", inventoryId)
                                .with(csrf()))


                .andExpect(status().isNoContent());


        // Verify
        verify(inventoryService)
                .activateInventory(inventoryId);
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnNotFoundWhenActivatingNonExistingInventory() throws Exception {

        // Arrange
        Long inventoryId = 999L;

        doThrow(new ResourceNotFoundException("Inventory not found"))
                .when(inventoryService)
                .activateInventory(inventoryId);


        // Act + Assert
        mockMvc.perform(
                        put("/api/v1/inventories/{id}/activate", inventoryId)
                                .with(csrf()))


                .andExpect(status().isNotFound());


        // Verify
        verify(inventoryService)
                .activateInventory(inventoryId);
    }
    @Test
    void shouldReturnUnauthorizedWhenActivatingWithoutAuthentication() throws Exception {

        // Arrange
        Long inventoryId = InventoryTestData.entityWithId().getId();

        // Act & Assert
        mockMvc.perform(put("/api/v1/inventories/{id}/activate", inventoryId)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());

        // Verify
        verifyNoInteractions(inventoryService);
    }
    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenWhenUserDoesNotHavePermissionToActivateInventory() throws Exception {

        // Arrange
        Long inventoryId = InventoryTestData.entityWithId().getId();

        // Act & Assert
        mockMvc.perform(put("/api/v1/inventories/{id}/activate", inventoryId)

                        .with(csrf()))
                .andExpect(status().isForbidden());

        // Verify
        verifyNoInteractions(inventoryService);
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldDeactivateInventorySuccessfully() throws Exception {

        // Arrange
        Long inventoryId = InventoryTestData.entityWithId().getId();

        doNothing().when(inventoryService)
                .deactivateInventory(inventoryId);

        // Act & Assert
        mockMvc.perform(put("/api/v1/inventories/{id}/deactivate", inventoryId)
                .with(csrf()))
                .andExpect(status().isNoContent());

        // Verify
        verify(inventoryService)
                .deactivateInventory(inventoryId);
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnNotFoundWhenDeactivatingNonExistingInventory() throws Exception {

        // Arrange
        Long inventoryId = InventoryTestData.entityWithId().getId();

        doThrow(new ResourceNotFoundException("Inventory not found"))
                .when(inventoryService)
                .deactivateInventory(inventoryId);

        // Act & Assert
        mockMvc.perform(put("/api/v1/inventories/{id}/deactivate", inventoryId)
                .with(csrf()))
                .andExpect(status().isNotFound());

        // Verify
        verify(inventoryService)
                .deactivateInventory(inventoryId);
    }

    @Test
    void shouldReturnUnauthorizedWhenDeactivatingWithoutAuthentication() throws Exception {

        // Arrange
        Long inventoryId = InventoryTestData.entityWithId().getId();

        // Act & Assert
        mockMvc.perform(put("/api/v1/inventories/{id}/deactivate", inventoryId)
                .with(csrf()))
                .andExpect(status().isUnauthorized());

        // Verify
        verifyNoInteractions(inventoryService);
    }
    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenWhenUserDoesNotHavePermissionToDeactivateInventory() throws Exception {

        // Arrange
        Long inventoryId = InventoryTestData.entityWithId().getId();

        // Act & Assert
        mockMvc.perform(put("/api/v1/inventories/{id}/deactivate", inventoryId)
                .with(csrf()))
                .andExpect(status().isForbidden());

        // Verify
        verifyNoInteractions(inventoryService);
    }






}
