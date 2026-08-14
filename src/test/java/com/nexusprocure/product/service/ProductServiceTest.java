package com.nexusprocure.product.service;
import com.nexusprocure.common.Response.PageResponse;
import com.nexusprocure.product.dto.request.ProductFilterRequest;
import com.nexusprocure.product.dto.update.ProductUpdateRequest;
import com.nexusprocure.product.testdata.ProductUpdateTestData;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentCaptor.forClass;
import com.nexusprocure.exception.ResourceNotFoundException;
import com.nexusprocure.product.dto.request.ProductRequest;
import com.nexusprocure.product.dto.response.ProductResponse;
import com.nexusprocure.product.entity.Category;
import com.nexusprocure.product.entity.Product;
import com.nexusprocure.product.mapper.ProductMapper;
import com.nexusprocure.product.repository.CategoryRepository;
import com.nexusprocure.product.repository.ProductRepository;
import com.nexusprocure.product.service.impl.ProductServiceImpl;
import com.nexusprocure.product.testdata.CategoryTestData;
import com.nexusprocure.product.testdata.ProductTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private ProductServiceImpl productService;
    private void assertProductResponse(ProductResponse expected,
                                       ProductResponse actual) {

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getProductCode(), actual.getProductCode());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getCategoryId(), actual.getCategoryId());
        assertEquals(expected.getCategoryName(), actual.getCategoryName());
        assertEquals(expected.getStatus(), actual.getStatus());
    }

    @Test
    void shouldCreateProductSuccessfully(){
        //Arrange
        // Arrange
        ProductRequest request = ProductTestData.request();

        Category category = CategoryTestData.entity();

        Product newProduct = ProductTestData.entity();
        newProduct.setId(null);
        newProduct.setProductCode(null);
        newProduct.setProductStatus(null);

        Product savedProduct = ProductTestData.entity();

        ProductResponse expectedResponse = ProductTestData.response();

        when(categoryRepository.findById(request.getCategoryId()))
                .thenReturn(Optional.of(category));

        when(productMapper.toEntity(request))
                .thenReturn(newProduct);

        when(productRepository.findTopByOrderByIdDesc())
                .thenReturn(Optional.empty());

        when(productRepository.save(any(Product.class)))
                .thenReturn(savedProduct);

        when(productMapper.toResponse(savedProduct))
                .thenReturn(expectedResponse);

        // Act
        ProductResponse actualResponse =
                productService.createProduct(request);

        // Assert
        assertProductResponse(expectedResponse, actualResponse);

        // Verify
        verify(categoryRepository).findById(request.getCategoryId());
        verify(productMapper).toEntity(request);
        verify(productRepository).findTopByOrderByIdDesc();

        ArgumentCaptor<Product> captor =
                ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(captor.capture());

        Product captured = captor.getValue();

        assertEquals(category, captured.getCategory());
        assertEquals(request.getName(), captured.getName());
        assertEquals(request.getDescription(), captured.getDescription());
        assertEquals(request.getPrice(), captured.getPrice());
        assertEquals("PRD-00001", captured.getProductCode());

        verify(productMapper).toResponse(savedProduct);

        verifyNoMoreInteractions(
                productRepository,
                categoryRepository,
                productMapper
        );

    }
    @Test
    void shouldThrowResourceNotFoundExceptionWhenCategoryDoesNotExist(){
        //Arrange
        ProductRequest request = ProductTestData.request();

        when(categoryRepository.findById(request.getCategoryId()))
                .thenReturn(Optional.empty());

        // Act + Assert
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> productService.createProduct(request)
                );

        assertEquals("Category not found", exception.getMessage());

        // Verify
        verify(categoryRepository).findById(request.getCategoryId());

        verifyNoInteractions(productMapper);

        verify(productRepository, never()).save(any());

        verify(productMapper, never()).toResponse(any());


    }
    @Test
    void shouldUpdateProductSuccessfully(){
        // Arrange
        Long productId = 1L;

        ProductUpdateRequest request =
                ProductUpdateTestData.request();

        Category category = CategoryTestData.entity();

        Product existingProduct = ProductTestData.entity();

        ProductResponse expectedResponse =
                ProductTestData.response();

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(existingProduct));

        when(categoryRepository.findById(request.getCategoryId()))
                .thenReturn(Optional.of(category));

        doNothing().when(productMapper)
                .updateEntity(request, existingProduct);

        when(productMapper.toResponse(existingProduct))
                .thenReturn(expectedResponse);

        // Act
        ProductResponse actualResponse =
                productService.updateProduct(productId, request);

        // Assert
        assertProductResponse(expectedResponse, actualResponse);

        // Verify
        verify(productRepository).findById(productId);

        verify(categoryRepository)
                .findById(request.getCategoryId());

        verify(productMapper)
                .updateEntity(request, existingProduct);

        verify(productMapper)
                .toResponse(existingProduct);

        verifyNoMoreInteractions(
                productRepository,
                categoryRepository,
                productMapper
        );
    }
    @Test
    void shouldThrowResourceNotFoundWhenProductDoesNotExist() {

        // Arrange
        Long productId = 999L;

        ProductUpdateRequest request =
                ProductUpdateTestData.request();

        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        // Act + Assert
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> productService.updateProduct(productId, request)
                );

        assertEquals("Product not found", exception.getMessage());

        // Verify
        verify(productRepository).findById(productId);

        verifyNoInteractions(categoryRepository);

        verifyNoInteractions(productMapper);
    }


    @Test
    void shouldThrowResourceNotFoundExceptionWhenCategoryDoesNotExistDuringUpdate() {

        // Arrange
        Long productId = 1L;

        ProductUpdateRequest request =
                ProductUpdateTestData.request();

        Product existingProduct =
                ProductTestData.entity();

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(existingProduct));

        when(categoryRepository.findById(request.getCategoryId()))
                .thenReturn(Optional.empty());

        // Act + Assert
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> productService.updateProduct(productId, request)
                );

        assertEquals("Category not found", exception.getMessage());

        // Verify
        verify(productRepository).findById(productId);

        verify(categoryRepository)
                .findById(request.getCategoryId());

        verifyNoInteractions(productMapper);
    }
    @Test
    void shouldGetProductByIdSuccessfully() {

        // Arrange
        Long productId = 1L;

        Product product = ProductTestData.entity();

        ProductResponse expectedResponse = ProductTestData.response();

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        when(productMapper.toResponse(product))
                .thenReturn(expectedResponse);

        // Act
        ProductResponse actualResponse =
                productService.getProductById(productId);

        // Assert
        assertProductResponse(expectedResponse, actualResponse);

        // Verify
        verify(productRepository).findById(productId);
        verify(productMapper).toResponse(product);

        verifyNoMoreInteractions(
                productRepository,
                productMapper
        );
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenProductNotFoundById() {

        // Arrange
        Long productId = 999L;

        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        // Act + Assert
        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> productService.getProductById(productId)
                );

        assertEquals("Product not found", exception.getMessage());

        // Verify
        verify(productRepository).findById(productId);

        verifyNoInteractions(productMapper);
    }

    @Test
    void shouldReturnPagedProductsSuccessfully() {

        // Arrange
        ProductFilterRequest request =
                ProductTestData.emptyFilter();

        Pageable pageable = PageRequest.of(0, 10);

        Product product1 = ProductTestData.entity();
        Product product2 = ProductTestData.entity();

        Page<Product> page =
                new PageImpl<>(
                        List.of(product1, product2),
                        pageable,
                        2
                );

        when(productRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(page);

        when(productMapper.toResponse(product1))
                .thenReturn(ProductTestData.response());

        when(productMapper.toResponse(product2))
                .thenReturn(ProductTestData.response());

        // Act
        PageResponse<ProductResponse> result =
                productService.getAllProducts(request, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getPage());
        assertEquals(10, result.getSize());

        // Verify
        verify(productRepository)
                .findAll(any(Specification.class), eq(pageable));

        verify(productMapper, times(2))
                .toResponse(any(Product.class));

        verifyNoMoreInteractions(
                productRepository,
                productMapper
        );
    }


    @Test
    void shouldReturnEmptyPageWhenNoProductsFound() {

        // Arrange
        ProductFilterRequest request =
                ProductTestData.emptyFilter();

        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> emptyPage = Page.empty(pageable);

        when(productRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(emptyPage);

        // Act
        PageResponse<ProductResponse> result =
                productService.getAllProducts(request, pageable);

        // Assert
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());

        // Verify
        verify(productRepository)
                .findAll(any(Specification.class), eq(pageable));

        verifyNoInteractions(productMapper);
    }
}