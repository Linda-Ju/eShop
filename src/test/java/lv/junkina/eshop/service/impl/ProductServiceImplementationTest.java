package lv.junkina.eshop.service.impl;

import lv.junkina.eshop.mappers.ProductMapStructMapper;
import lv.junkina.eshop.model.Product;
import lv.junkina.eshop.repository.ProductRepository;
import lv.junkina.eshop.repository.model.ProductDAO;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class ProductServiceImplementationTest {

    @InjectMocks
    private ProductServiceImplementation productServiceImpl;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapStructMapper productMapper;
    @Rule
    public final ExpectedException exception = ExpectedException.none();


    private Product product;
    private ProductDAO productDAO;
    private List<Product> productList;
    private List<ProductDAO> productDAOList;

    @BeforeEach
    public void init() {
        product = getProduct(1L, "name", 13.99, 6, "description", "photo");
        productDAO = getProductDAO(1L, "name", 13.99, 6, "description", "photo");
        productList = getProductList(product);
        productDAOList = getProductDAOList(productDAO);
    }

    @Test
    public void getALlProductSuccess() {
        when(productRepository.findAll()).thenReturn(productDAOList);
        when(productMapper.productDAOToProduct(productDAO)).thenReturn(product);
        List<Product> productList = productServiceImpl.getAllProducts();
        assertEquals(2, productList.size());
        assertEquals(productList.get(0).getName(), "name");
    }

    @Test
    void getAllProductsUnsuccessful() {
        when(productRepository.findAll()).thenReturn(new ArrayList<>());
        List<Product> list = productServiceImpl.getAllProducts();
        assertEquals(0, list.size());
    }

    @Test
    void getProductByIdSuccess() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(productDAO));
        when(productMapper.productDAOToProduct(productDAO)).thenReturn(product);
        Optional<Product> optionalProjectRole = productServiceImpl.getProductById(anyLong());
        optionalProjectRole.ifPresent(projectRole -> assertEquals("name", projectRole.getName()));
    }

    @Test
    void getProductByIdUnsuccessful() {
        when(productRepository.findById(any())).thenReturn(Optional.empty());
        Optional<Product> product = productServiceImpl.getProductById(any());
        Assertions.assertFalse(product.isPresent());
    }

    @Test
    void saveProductSuccess() {
        when(productRepository.save(productDAO)).thenReturn(productDAO);
        when(productMapper.productDAOToProduct(productDAO)).thenReturn(product);
        when(productMapper.productToProductDAO(product)).thenReturn(productDAO);
        Product savedProduct = productServiceImpl.saveProduct(product);
//        assertEquals("name", savedProduct.getName());
        assertAll("product",
                () -> assertEquals(productDAO.getId(), savedProduct.getId()),
                () -> assertEquals(productDAO.getName(), savedProduct.getName()),
                () -> assertEquals(productDAO.getPrice(), savedProduct.getPrice()),
                () -> assertEquals(productDAO.getQuantity(), savedProduct.getQuantity()),
                () -> assertEquals(productDAO.getDescription(), savedProduct.getDescription())
        );
        verify(productRepository, times(1)).save(productDAO);
    }

    @Test
    void saveProductUnsuccessful() {
        when(productRepository.findAll()).thenThrow(new HttpClientErrorException(HttpStatus.CONFLICT));
        try {
            productServiceImpl.saveProduct(product);
        } catch (Exception e) {
            assertEquals("409 CONFLICT", e.getMessage());
        }
    }

    @Test
    void deleteProductSuccess() {
        productServiceImpl.deleteProductById(anyLong());
        verify(productRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteProductUnsuccessful() {
        productServiceImpl.deleteProductById(null);
        exception.expect(IllegalArgumentException.class);
    }


    public Product getProduct(Long id, String name, double price, int qty, String description, String photo) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        product.setQuantity(qty);
        product.setDescription(description);
        product.setPhoto(photo);
        return product;
    }

    public ProductDAO getProductDAO(Long id, String name, double price, int qty, String description, String photo) {
        ProductDAO product = new ProductDAO();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        product.setQuantity(qty);
        product.setDescription(description);
        product.setPhoto(photo);
        return product;
    }

    public List<Product> getProductList(Product product) {
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product);
        return productList;
    }

    public List<ProductDAO> getProductDAOList(ProductDAO product) {
        List<ProductDAO> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product);
        return productList;
    }
}