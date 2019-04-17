package org.yanmark.markoni.unitTest.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.yanmark.markoni.domain.entities.Product;
import org.yanmark.markoni.domain.models.bindings.products.ProductCreateBindingModel;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.repositories.CategoryRepository;
import org.yanmark.markoni.repositories.ProductRepository;
import org.yanmark.markoni.services.CategoryService;
import org.yanmark.markoni.services.CloudinaryService;
import org.yanmark.markoni.services.ProductService;
import org.yanmark.markoni.utils.TestUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

    @MockBean
    private ProductRepository mockProductRepository;

    @MockBean
    private CategoryRepository mockCategoryRepository;

    @Autowired
    private ProductService productService;

    @MockBean
    private Cloudinary mockCloudinary;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ModelMapper modelMapper;

    //TODO implement mock cloudinary
//    @Test
//    public void saveProduct_whenSaveProduct_returnPersistedProduct() throws IOException {
//        Product testProduct = TestUtils.getTestProduct();
//        when(mockCategoryRepository.findAllOrderByName())
//                .thenReturn(TestUtils.getTestCategories(2));
//        when(mockProductRepository.saveAndFlush(any(Product.class)))
//                .thenReturn(testProduct);
//
//        ProductServiceModel productServiceModel = modelMapper.map(testProduct, ProductServiceModel.class);
//        ProductCreateBindingModel productCreateBindingModel = modelMapper.map(testProduct, ProductCreateBindingModel.class);
//
//        ProductServiceModel result = productService.saveProduct(productServiceModel, productCreateBindingModel);
//
//        assertEquals(testProduct.getId(), result.getId());
//    }

    @Test(expected = Exception.class)
    public void saveProduct_whenProductIsNull_throwException() throws IOException {
        productService.saveProduct(null, null);
        verify(mockProductRepository).saveAndFlush(any());
    }

    @Test(expected = Exception.class)
    public void saveProduct_whenProductExist_throwException() throws IOException {
        Product testProduct = TestUtils.getTestProduct();
        when(mockProductRepository.findByName(anyString()))
                .thenReturn(Optional.of(testProduct));

        ProductCreateBindingModel productCreateBindingModel = modelMapper.map(testProduct, ProductCreateBindingModel.class);
        ProductServiceModel productServiceModel = modelMapper.map(testProduct, ProductServiceModel.class);

        productService.saveProduct(productServiceModel, productCreateBindingModel);

        verify(mockProductRepository).saveAndFlush(any());
    }

    @Test
    public void editProduct_whenEditProduct_returnEditedProduct() {
        Product testProduct = TestUtils.getTestProduct();
        Product editProduct = new Product() {{
            setWeight(3.5);
            setDescription("testDescription");
            setPrice(BigDecimal.valueOf(50));
        }};
        when(mockProductRepository.findById(anyString()))
                .thenReturn(Optional.of(testProduct));
        when(mockProductRepository.saveAndFlush(any(Product.class)))
                .thenReturn(editProduct);
        ProductServiceModel productServiceModel = modelMapper.map(editProduct, ProductServiceModel.class);

        ProductServiceModel editProductServiceModel = productService.editProduct(productServiceModel, testProduct.getId());

        assertEquals(Double.valueOf(3.5), editProductServiceModel.getWeight());
        assertEquals("testDescription", editProductServiceModel.getDescription());
        assertEquals(BigDecimal.valueOf(50), editProductServiceModel.getPrice());
    }

    @Test
    public void deleteProduct_whenDeleteProduct_void() {
        Product testProduct = TestUtils.getTestProduct();

        productService.deleteProduct(testProduct.getId());

        verify(mockProductRepository).deleteById(testProduct.getId());
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
        when(mockProductRepository.findByName(anyString()))
                .thenReturn(Optional.of(testProduct));

        ProductServiceModel productServiceModel = productService.getProductByName(testProduct.getName());

        assertEquals(testProduct.getName(), productServiceModel.getName());
        assertEquals(testProduct.getId(), productServiceModel.getId());
    }

    @Test(expected = Exception.class)
    public void getProductByName_whenNoFindProductByName_throwException() {
        Product testProduct = TestUtils.getTestProduct();

        productService.getProductByName(testProduct.getName());

        verify(mockProductRepository).findByName(anyString());
    }

    @Test
    public void getProductById_whenFindProductById_returnProduct() {
        Product testProduct = TestUtils.getTestProduct();
        when(mockProductRepository.findById(anyString()))
                .thenReturn(Optional.of(testProduct));

        ProductServiceModel productServiceModel = productService.getProductById(testProduct.getId());

        assertEquals(testProduct.getId(), productServiceModel.getId());
        assertEquals(testProduct.getName(), productServiceModel.getName());
    }

    @Test(expected = Exception.class)
    public void getProductById_whenNoFindProductById_throwException() {
        Product testProduct = TestUtils.getTestProduct();

        productService.getProductById(testProduct.getId());

        verify(mockProductRepository).findById(anyString());
    }

    @Test
    public void getAllProductsByName_when1Products_return1Products() {
        String productName = "model";
        when(mockProductRepository.findAllByName(anyString()))
                .thenReturn(TestUtils.getTestProducts(2));

        List<ProductServiceModel> result = productService.getAllProductsByName(productName);

        assertEquals(2, result.size());
    }

    @Test
    public void getAllProductsByName_whenNoProducts_returnNoProducts() {
        String productName = "model";
        when(mockProductRepository.findAllByName(anyString()))
                .thenReturn(new ArrayList<>());

        List<ProductServiceModel> result = productService.getAllProductsByName(productName);

        assertEquals(0, result.size());
    }
}
