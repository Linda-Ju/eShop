package lv.junkina.eshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lv.junkina.eshop.model.Product;
import lv.junkina.eshop.service.impl.ProductServiceImplementation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    public static String URL = "/api/product";

    private Product product;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductController controller;

    @MockBean
    private ProductServiceImplementation serviceImpl;

    public List<Product> productList = new ArrayList<>();


    @Test
    void getAllProducts() throws Exception {
        productList.add(testProduct());
        when(serviceImpl.getAllProducts()).thenReturn(productList);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllProductsNoData() throws Exception {
        when(serviceImpl.getAllProducts()).thenReturn(Collections.EMPTY_LIST);
        mockMvc.perform(get(URL+ "/all"))
                .andExpect(status().isNotFound());
    }


    @Test
    public void getProductById() throws Exception {
        when(serviceImpl.getProductById(anyLong())).thenReturn(Optional.of(testProduct()));
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/" + anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getProductByIdNotFound() throws Exception {
        when(serviceImpl.getProductById(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/" + anyLong())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveProductSuccess() throws Exception {
        when(serviceImpl.saveProduct(any(Product.class))).thenReturn(testProduct());
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(getContent(testProduct()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void saveProductEmptyName() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContent(testEmptyName())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveProductEmptyPrice() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContent(testEmptyPrice())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveProductEmptyQuantity() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContent(testEmptyQuantity())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProductByIdSuccess() throws Exception {
        Product product = testProduct();
        when(serviceImpl.getProductById(product.getId())).thenReturn(Optional.of(product));
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(asJsonString(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                        .andExpect(status().isCreated());
        verify(serviceImpl, times(1)).saveProduct(product);
    }

    @Test
    void UpdateProductByIdUnsuccessful() throws Exception {
        Product product = testProduct();
        product.setId(null);
        when(serviceImpl.getProductById(null)).thenReturn(Optional.empty());
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/")
                        .content(asJsonString(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        verify(serviceImpl, times(0)).saveProduct(product);
    }

    @Test
    void deleteProduct() throws Exception {
        Optional<Product> product = Optional.of(testProduct());
        when(serviceImpl.getProductById(anyLong())).thenReturn(product);
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/1")
                        .content(asJsonString(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(serviceImpl, times(1)).deleteProductById(anyLong());
    }

    @Test
    void deleteProductUnsuccessful() throws Exception {
        Optional<Product> product = Optional.of(testProduct());
        product.get().setId(null);
        when(serviceImpl.getProductById(null)).thenReturn(product);
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + null)
                        .content(asJsonString(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(serviceImpl, times(0)).deleteProductById(null);
    }
    private Product testProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setPrice(BigDecimal.valueOf(15.99));
        product.setQuantity(5);
        product.setDescription("description");
        product.setPhoto("photo link");
        return product;
    }

    private Product testEmptyName() {
        Product product = new Product();
        product.setId(1L);
        product.setName("");
        product.setPrice(BigDecimal.valueOf(15.89));
        product.setQuantity(9);
        product.setDescription("description");
        product.setPhoto("photo link");
        return product;
    }

    private Product testEmptyPrice() {
        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setPrice(BigDecimal.valueOf(0));
        product.setQuantity(5);
        product.setDescription("description");
        product.setPhoto("photo link");
        return product;
    }

    private Product testEmptyQuantity() {
        Product product = new Product();
        product.setId(1L);
        product.setName("name");
        product.setPrice(BigDecimal.valueOf(15.99));
        product.setQuantity(0);
        product.setDescription("description");
        product.setPhoto("photo link");
        return product;
    }

    private List<Product> testProductList() {
        List<Product> list = new ArrayList<>();
        return list;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getContent(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}