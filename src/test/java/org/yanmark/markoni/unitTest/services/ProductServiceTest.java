package org.yanmark.markoni.unitTest.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.yanmark.markoni.domain.entities.Category;
import org.yanmark.markoni.domain.entities.Product;
import org.yanmark.markoni.domain.models.bindings.products.ProductCreateBindingModel;
import org.yanmark.markoni.domain.models.services.CategoryServiceModel;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.repositories.CategoryRepository;
import org.yanmark.markoni.repositories.ProductRepository;
import org.yanmark.markoni.services.*;
import org.yanmark.markoni.utils.TestUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository mockProductRepository;

    @Mock
    private CategoryRepository mockCategoryRepository;

    private CategoryService categoryService;

    @Mock
    private CloudinaryService mockCloudinaryService;

    private ProductService productService;

    private ModelMapper modelMapper;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
        categoryService = new CategoryServiceImpl(mockCategoryRepository, modelMapper);
        productService = new ProductServiceImpl(mockProductRepository, categoryService, mockCloudinaryService, modelMapper);
    }

//    @Test
//    public void saveProduct_whenSaveProduct_returnPersistedProduct() throws IOException {
//        Product testProduct = TestUtils.getTestProduct();
//        String productName = testProduct.getName();
//        List<Category> testCategories = TestUtils.getTestCategories(2);
//        Category testCategory = new Category() {{
//            setId("0x");
//            setName("model 0");
//        }};
//        when(mockCategoryRepository.findAllOrderByName())
//                .thenReturn(testCategories);
//        MockMultipartFile file = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
//        when(mockCloudinaryService.uploadImage(file))
//                .thenReturn(anyString());
//        ProductCreateBindingModel productCreateBindingModel = modelMapper.map(testProduct, ProductCreateBindingModel.class);
//        productCreateBindingModel.setCategories(Set.of(testCategory.getId()));
//        CategoryServiceModel categoryServiceModel = modelMapper.map(testCategory, CategoryServiceModel.class);
//        Set<CategoryServiceModel> categoriesServiceModels = new HashSet<>();
//        categoriesServiceModels.add(categoryServiceModel);
//        ProductServiceModel productServiceModel = modelMapper.map(testProduct, ProductServiceModel.class);
//        productServiceModel.setCategories(categoriesServiceModels);
//        productServiceModel.setImage(anyString());
//
//        ProductServiceModel persistedModel = productService.saveProduct(productServiceModel, productCreateBindingModel);
//
//        verify(mockProductRepository).findByName(productName);
//        assertEquals(productServiceModel.getId(), persistedModel.getId());
//    }

    @Test(expected = Exception.class)
    public void saveProduct_whenProductIsNull_throwException() throws IOException {
        productService.saveProduct(null, null);
        verify(mockProductRepository).saveAndFlush(any());
    }

    @Test(expected = Exception.class)
    public void saveProduct_whenProductExist_throwException() throws IOException {
        Product testProduct = TestUtils.getTestProduct();
        when(mockProductRepository.findByName(Mockito.anyString()))
                .thenReturn(Optional.of(testProduct));

        ProductCreateBindingModel productCreateBindingModel = modelMapper.map(testProduct, ProductCreateBindingModel.class);
        ProductServiceModel productServiceModel = modelMapper.map(testProduct, ProductServiceModel.class);
        productService.saveProduct(productServiceModel, productCreateBindingModel);

        verify(mockProductRepository).saveAndFlush(any());
    }

    @Test
    public void editProduct_whenEditProduct_returnEditedProduct() {
        Product beforeEditProduct = TestUtils.getTestProduct();
        Product editProduct = new Product() {{
            setWeight(3.5);
            setDescription("testDescription");
            setPrice(BigDecimal.valueOf(50));
        }};
        String productId = beforeEditProduct.getId();
        when(mockProductRepository.findById(productId))
                .thenReturn(Optional.of(beforeEditProduct));
        when(mockProductRepository.saveAndFlush(Mockito.any(Product.class)))
                .thenReturn(editProduct);
        ProductServiceModel productServiceModel = modelMapper.map(editProduct, ProductServiceModel.class);

        ProductServiceModel editProductServiceModel = productService.editProduct(productServiceModel, productId);

        assertEquals(Double.valueOf(3.5), editProductServiceModel.getWeight());
        assertEquals("testDescription", editProductServiceModel.getDescription());
        assertEquals(BigDecimal.valueOf(50), editProductServiceModel.getPrice());
    }

    @Test
    public void deleteProduct_whenDeleteProduct_void() {
        Product testProduct = TestUtils.getTestProduct();

        String productId = testProduct.getId();
        productService.deleteProduct(productId);

        verify(mockProductRepository).deleteById(productId);
    }

    @Test
    public void getAllProducts_when2Products_return2Products() {
        when(mockProductRepository.findAllOrdered())
                .thenReturn(TestUtils.getTestProducts(2));

        List<ProductServiceModel> result = productService.getAllProducts();

        assertEquals(2, result.size());
    }

    @Test
    public void getAllProducts_whenNoProducts_returnEmptyProducts() {
        when(mockProductRepository.findAllOrdered())
                .thenReturn(new ArrayList<>());

        List<ProductServiceModel> result = productService.getAllProducts();

        assertEquals(0, result.size());
    }

    @Test
    public void getProductByName_whenFindProductByName_returnProduct() {
        Product testProduct = TestUtils.getTestProduct();
        String productName = testProduct.getName();
        when(mockProductRepository.findByName(productName))
                .thenReturn(Optional.of(testProduct));

        ProductServiceModel productServiceModel = productService.getProductByName(productName);

        assertNotNull(testProduct);
        assertEquals(productName, productServiceModel.getName());
        assertEquals(testProduct.getId(), productServiceModel.getId());
    }

    @Test(expected = Exception.class)
    public void getProductByName_whenNoFindProductByName_throwException() {
        Product testProduct = TestUtils.getTestProduct();
        String productName = testProduct.getName();

        productService.getProductByName(productName);

        verify(mockProductRepository).findByName(anyString());
    }

    @Test
    public void getProductById_whenFindProductById_returnProduct() {
        Product testProduct = TestUtils.getTestProduct();
        String productId = testProduct.getId();
        when(mockProductRepository.findById(productId))
                .thenReturn(Optional.of(testProduct));

        ProductServiceModel productServiceModel = productService.getProductById(productId);

        assertNotNull(testProduct);
        assertEquals(productId, productServiceModel.getId());
        assertEquals(testProduct.getName(), productServiceModel.getName());
    }

    @Test(expected = Exception.class)
    public void getProductById_whenNoFindProductById_throwException() {
        Product testProduct = TestUtils.getTestProduct();
        String productId = testProduct.getId();

        productService.getProductById(productId);

        verify(mockProductRepository).findById(anyString());
    }

    @Test
    public void getAllProductsByName_when1Products_return1Products() {
        String productName = "model";
        when(mockProductRepository.findAllByName(productName))
                .thenReturn(TestUtils.getTestProducts(2));

        List<ProductServiceModel> result = productService.getAllProductsByName(productName);

        assertEquals(2, result.size());
    }

    @Test
    public void getAllProductsByName_whenNoProducts_returnNoProducts() {
        String productName = "model";
        when(mockProductRepository.findAllByName(productName))
                .thenReturn(new ArrayList<>());

        List<ProductServiceModel> result = productService.getAllProductsByName(productName);

        assertEquals(0, result.size());
    }
}
