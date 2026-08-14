package com.nexusprocure.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexusprocure.authentication.Service.CustomUserDetailsService;
import com.nexusprocure.authentication.Service.JwtService;
import com.nexusprocure.common.Response.PageResponse;
import com.nexusprocure.config.*;
import com.nexusprocure.exception.DuplicateResourceException;
import com.nexusprocure.exception.GlobalExceptionalHandler;
import com.nexusprocure.exception.ResourceNotFoundException;
import com.nexusprocure.product.dto.request.ProductFilterRequest;
import com.nexusprocure.product.dto.request.ProductRequest;
import com.nexusprocure.product.dto.response.ProductResponse;
import com.nexusprocure.product.dto.update.ProductUpdateRequest;
import com.nexusprocure.product.service.ProductService;
import com.nexusprocure.product.testdata.ProductTestData;
import com.nexusprocure.product.testdata.ProductUpdateTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(
        controllers = ProductController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = SecurityConfig.class
        ))

@Import({
        TestSecurityConfig.class,
        GlobalExceptionalHandler.class,
        CustomAuthenticationEntryPoint.class,
        CustomAccessDeniedHandler.class
})
@ImportAutoConfiguration(exclude = {
        SecurityConfig.class
})

public class ProductControllerTest {


    @Autowired
    private MockMvc mockMvc;// it exactly simulates like Postman or swaggerUi without running the server
    @Autowired
    private ObjectMapper objectMapper;// to map java object to json and json to java objects.
    @MockitoBean
    private ProductService productService;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateProductSuccessfully() throws Exception{
        //Arrange
        ProductRequest request = ProductTestData.request();
        ProductResponse response = ProductTestData.response();
        when(productService.createProduct(any(ProductRequest.class)))
                .thenReturn(response);

        //Act+Assert
        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productCode").value("PRD-00001"))
                .andExpect(jsonPath("$.name").value("Dell Laptop"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
        //Verify
        verify(productService, times(1)).createProduct(any(ProductRequest.class));



    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRejectBlankProductName()throws Exception{
        //Arrange
        ProductRequest request = ProductTestData.request();
        request.setName("");
        //Act+Assert
        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Product name is required"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.path").value("/api/v1/products"));


        //Verify
        verifyNoMoreInteractions(productService);


    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldTranslateResourceNotFoundExceptionToNotFoundResponse()throws Exception{
        //Arrange
        ProductRequest request = ProductTestData.request();
        when(productService.createProduct(any(ProductRequest.class)))
                .thenThrow(new ResourceNotFoundException("Category not found"));
        //Act+Assert
        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Category not found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.path").value("/api/v1/products"));
        //Verify
        verify(productService).createProduct(any(ProductRequest.class));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldTranslateDuplicateResourceExceptionToConflictResponse()throws Exception{
        //Arrange
        ProductRequest request = ProductTestData.request();
        when(productService.createProduct(any(ProductRequest.class)))
                .thenThrow(new DuplicateResourceException("Product already exists"));
        //Act+Assert
        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Product already exists"))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.path").value("/api/v1/products"));
        //Verify
        verify(productService).createProduct(any(ProductRequest.class));
    }
    @Test
    void shouldReturnUnauthorizedWhenUserIsNotAuthenticated()throws Exception{
        //Arrange
        ProductRequest request = ProductTestData.request();
        //Act+Asset
        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Authentication required"))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.path").value("/api/v1/products"))
                .andExpect(jsonPath("$.timestamp").exists());
        //Verify
        verifyNoInteractions(productService);


    }
    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenWhenUserDoesNotHaveRequiredRole()throws Exception {


        //Arrange
        ProductRequest request = ProductTestData.request();
        //Act+Assert
        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access Denied"))
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.error").value("Forbidden"))
                .andExpect(jsonPath("$.path").value("/api/v1/products"))
                .andExpect(jsonPath("$.timestamp").exists());
      //Verify
        verifyNoInteractions(productService);

    }
    @Test
    @WithMockUser
    void shouldReturnProductWhenProductExists()throws Exception{
        //Arrange
        ProductResponse response = ProductTestData.response();
        when(productService.getProductById(1L))
                .thenReturn(response);
        //Act+Assert
        mockMvc.perform(get("/api/v1/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.productCode").value(response.getProductCode()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.description").value(response.getDescription()))
                .andExpect(jsonPath("$.price").value(response.getPrice()))
                .andExpect(jsonPath("$.categoryId").value(response.getCategoryId()))
                .andExpect(jsonPath("$.categoryName").value(response.getCategoryName()))
                .andExpect(jsonPath("$.status").value(response.getStatus().name()));
        //verify
        verify(productService).getProductById(1L);

    }
    @Test
    @WithMockUser
    void shouldReturnNotFoundResponseWhenProductDoesNotExist()throws Exception{
        //Arrange
        when(productService.getProductById(1L))
                .thenThrow(new ResourceNotFoundException("Product not found"));
        //Act+Assert
        mockMvc.perform(get("/api/v1/products/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.path").value("/api/v1/products/1"))
                .andExpect(jsonPath("$.timestamp").exists());
        //verify
        verify(productService).getProductById(1L);



    }
    @Test
    void shouldReturnUnauthorizedWhenGettingProductWithoutAuthentication()throws Exception{
        //Arrange
        // No arrange because its immediately blocked
        mockMvc.perform(
                        get("/api/v1/products/{id}", 1L)
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value("Authentication required"))
                .andExpect(jsonPath("$.status")
                        .value(401))
                .andExpect(jsonPath("$.error")
                        .value("Unauthorized"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/products/1"))
                .andExpect(jsonPath("$.timestamp")
                        .exists());
        //Verify
        verifyNoInteractions(productService);
    }
    @Test
    @WithMockUser
    void shouldReturnPagedProductSuccessfully()throws Exception{
        //Arrange
        //In controller what mock services return we use that
        PageResponse<ProductResponse> response = ProductTestData.pageResponse();
        when(productService.getAllProducts(any(ProductFilterRequest.class),any(Pageable.class)))
                .thenReturn(response);
        //Act+Assert
        mockMvc.perform(get("/api/v1/products").param("page", "0")
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
        verify(productService).getAllProducts(any(ProductFilterRequest.class), any(Pageable.class));

    }
    @Test
    void shouldReturnUnauthorizedWhenGettingProductsWithoutAuthentication() throws Exception{
        //There is no Arrange and stub when because it never reaches the service
        //Act+Assert
        mockMvc.perform(get("/api/v1/products")
                .param("page", "0")
                .param("size", "10")
        )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value("Authentication required"))
                .andExpect(jsonPath("$.status")
                        .value(401))
                .andExpect(jsonPath("$.error")
                        .value("Unauthorized"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/products"))
                .andExpect(jsonPath("$.timestamp")
                        .exists());
        //Verify
        verifyNoInteractions(productService);
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldUpdateProductSuccessfully() throws Exception{
     //Arrange
        Long productId = 1L;
        ProductUpdateRequest request = ProductUpdateTestData.request();
        ProductResponse response = ProductTestData.response();
        when(productService.updateProduct(eq(productId), any(ProductUpdateRequest.class)))
                .thenReturn(response);
        //Act+Assert
        mockMvc.perform(put("/api/v1/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(response.getId()))
                .andExpect(jsonPath("$.productCode")
                        .value(response.getProductCode()))
                .andExpect(jsonPath("$.name")
                        .value(response.getName()))
                .andExpect(jsonPath("$.description")
                        .value(response.getDescription()))
                .andExpect(jsonPath("$.price")
                        .value(response.getPrice()))
                .andExpect(jsonPath("$.categoryId")
                        .value(response.getCategoryId()))
                .andExpect(jsonPath("$.categoryName")
                        .value(response.getCategoryName()))
                .andExpect(jsonPath("$.status")
                        .value(response.getStatus().name()));
        //verify
        verify(productService).updateProduct(eq(productId), any(ProductUpdateRequest.class));


    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnBadRequestWhenUpdateRequestIsInvalid()throws Exception{
        //Arrange
        Long productId = 1L;
        ProductUpdateRequest request = ProductUpdateTestData.request();
        request.setName("");
        //Act+Assert
        mockMvc.perform(put("/api/v1/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Product name is required"))
                .andExpect(jsonPath("$.status")
                        .value(400))
                .andExpect(jsonPath("$.error")
                        .value("Bad Request"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/products/1"))
                .andExpect(jsonPath("$.timestamp")
                        .exists());
        //verify
        verifyNoInteractions(productService);

    }
@Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnNotFoundResponseWhenUpdatingNonExistingProduct()throws Exception{
        //Arrange
    Long productId = 1L;
    ProductUpdateRequest request = ProductUpdateTestData.request();
    when(productService.updateProduct(eq(productId), any(ProductUpdateRequest.class)))
            .thenThrow(new ResourceNotFoundException("Product not found"));
    //Act+Assert
    mockMvc.perform(put("/api/v1/products/{id}", productId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    )
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message")
                    .value("Product not found"))
            .andExpect(jsonPath("$.status")
                    .value(404))
            .andExpect(jsonPath("$.error")
                    .value("Not Found"))
            .andExpect(jsonPath("$.path")
                    .value("/api/v1/products/1"))
            .andExpect(jsonPath("$.timestamp")
                    .exists());
    //verify
    // FIX: was calling the throwing stub directly instead of verify()
    verify(productService).updateProduct(eq(productId), any(ProductUpdateRequest.class));

}
@Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnNotFoundResponseWhenUpdatingNonExistingCategory() throws Exception{
        //Arrange
    Long productId = 1L;
    ProductUpdateRequest request = ProductUpdateTestData.request();
    when(productService.updateProduct(eq(productId), any(ProductUpdateRequest.class)))
            .thenThrow(new ResourceNotFoundException("Category not found"));
    //Act+Assert
    mockMvc.perform(put("/api/v1/products/{id}", productId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    )
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Category not found"))
            .andExpect(jsonPath("$.status")
                    .value(404))
            .andExpect(jsonPath("$.error")
                    .value("Not Found"))
            .andExpect(jsonPath("$.path")
                    .value("/api/v1/products/1"))
            .andExpect(jsonPath("$.timestamp")
                    .exists());

    //Verify
    //Verify
    verify(productService).updateProduct(eq(productId), any(ProductUpdateRequest.class));
}
@Test
    void ShouldReturnUnAuthorizedWhenUpdatingProductWithoutAuthentication() throws Exception{
        //Arrange
    Long productId = 1L;
    ProductUpdateRequest request =
            ProductUpdateTestData.request();

    // Act & Assert

    mockMvc.perform(
                    put("/api/v1/products/{id}", productId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message")
                    .value("Authentication required"))
            .andExpect(jsonPath("$.status")
                    .value(401))
            .andExpect(jsonPath("$.error")
                    .value("Unauthorized"))
            .andExpect(jsonPath("$.path")
                    .value("/api/v1/products/1"))
            .andExpect(jsonPath("$.timestamp")
                    .exists());

    // Verify

    verifyNoInteractions(productService);

}
    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenWhenUserDoesNotHavePermissionToUpdateProduct() throws Exception {

        // Arrange

        Long productId = 1L;

        ProductUpdateRequest request =
                ProductUpdateTestData.request();

        // Act & Assert

        mockMvc.perform(
                        put("/api/v1/products/{id}", productId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message")
                        .value("Access Denied"))
                .andExpect(jsonPath("$.status")
                        .value(403))
                .andExpect(jsonPath("$.error")
                        .value("Forbidden"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/products/1"))
                .andExpect(jsonPath("$.timestamp")
                        .exists());

        // Verify

        verifyNoInteractions(productService);
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldActivateProductSuccessfully() throws Exception{
        Long productId = 1L;
        doNothing().when(productService).activateProduct(productId);
        //Act+Assert
        mockMvc.perform(put("/api/v1/products/{id}/activate", productId)
        )
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
        // Verify

        verify(productService)
                .activateProduct(productId);
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnNotFoundWhenActivatingNonExistingProduct() throws Exception {

        // Arrange

        Long productId = 1L;

        doThrow(new ResourceNotFoundException("Product not found"))
                .when(productService)
                .activateProduct(productId);

        // Act & Assert

        mockMvc.perform(
                        put("/api/v1/products/{id}/activate", productId)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Product not found"))
                .andExpect(jsonPath("$.status")
                        .value(404))
                .andExpect(jsonPath("$.error")
                        .value("Not Found"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/products/1/activate"))
                .andExpect(jsonPath("$.timestamp")
                        .exists());

        // Verify

        verify(productService)
                .activateProduct(productId);
    }
    @Test
    void shouldReturnUnauthorizedWhenActivatingProductWithoutAuthentication() throws Exception {

        // Arrange

        Long productId = 1L;

        // Act & Assert

        mockMvc.perform(
                        put("/api/v1/products/{id}/activate", productId)
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value("Authentication required"))
                .andExpect(jsonPath("$.status")
                        .value(401))
                .andExpect(jsonPath("$.error")
                        .value("Unauthorized"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/products/1/activate"))
                .andExpect(jsonPath("$.timestamp")
                        .exists());

        // Verify

        verifyNoInteractions(productService);

    }
    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenWhenUserDoesNotHavePermissionToActivateProduct() throws Exception {

        // Arrange

        Long productId = 1L;

        // Act & Assert

        mockMvc.perform(
                        put("/api/v1/products/{id}/activate", productId)
                )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message")
                        .value("Access Denied"))
                .andExpect(jsonPath("$.status")
                        .value(403))
                .andExpect(jsonPath("$.error")
                        .value("Forbidden"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/products/1/activate"))
                .andExpect(jsonPath("$.timestamp")
                        .exists());

        // Verify

        verifyNoInteractions(productService);
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldDeactivateProductSuccessfully() throws Exception {

        // Arrange

        Long productId = 1L;

        // Act & Assert

        mockMvc.perform(
                        put("/api/v1/products/{id}/deactivate", productId)
                )
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        // Verify

        verify(productService)
                .deactivateProduct(productId);
    }
    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnNotFoundWhenDeactivatingNonExistingProduct() throws Exception {

        // Arrange

        Long productId = 1L;

        doThrow(new ResourceNotFoundException("Product not found"))
                .when(productService)
                .deactivateProduct(productId);

        // Act & Assert

        mockMvc.perform(
                        put("/api/v1/products/{id}/deactivate", productId)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Product not found"))
                .andExpect(jsonPath("$.status")
                        .value(404))
                .andExpect(jsonPath("$.error")
                        .value("Not Found"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/products/1/deactivate"))
                .andExpect(jsonPath("$.timestamp")
                        .exists());

        // Verify

        verify(productService)
                .deactivateProduct(productId);
    }
    @Test
    void shouldReturnUnauthorizedWhenDeactivatingProductWithoutAuthentication() throws Exception {

        // Arrange

        Long productId = 1L;

        // Act & Assert

        mockMvc.perform(
                        put("/api/v1/products/{id}/deactivate", productId)
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value("Authentication required"))
                .andExpect(jsonPath("$.status")
                        .value(401))
                .andExpect(jsonPath("$.error")
                        .value("Unauthorized"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/products/1/deactivate"))
                .andExpect(jsonPath("$.timestamp")
                        .exists());

        // Verify

        verifyNoInteractions(productService);
    }
    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenWhenUserDoesNotHavePermissionToDeactivateProduct() throws Exception {

        // Arrange

        Long productId = 1L;

        // Act & Assert

        mockMvc.perform(
                        put("/api/v1/products/{id}/deactivate", productId)
                )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message")
                        .value("Access Denied"))
                .andExpect(jsonPath("$.status")
                        .value(403))
                .andExpect(jsonPath("$.error")
                        .value("Forbidden"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/products/1/deactivate"))
                .andExpect(jsonPath("$.timestamp")
                        .exists());

        // Verify

        verifyNoInteractions(productService);
    }

}
