package lv.junkina.eshop;

import lv.junkina.eshop.model.Product;
import lv.junkina.eshop.service.impl.ProductServiceImplementation;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductControllerIT {

    @Autowired
    private ProductServiceImplementation serviceImplementation;

    @Autowired
    private TestRestTemplate restTemplate;

//    @Test
//    void findAllProducts() throws JSONException {
//        String response = this.restTemplate.getForObject("/api/product/all", String.class);
//        JSONAssert.assertEquals("[{id:1}, {id:2}, {id:3}, {id:7}]", response, false);
//    }

    @Test
    void findProductById() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/api/product/{id}", String.class, 1);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void findProductByIdNotFound() {
        ResponseEntity<String> err = restTemplate.getForEntity("/api/product/{id}", String.class, 100);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, err.getStatusCode());
    }

    @Test
    void addNewProduct() throws JSONException {
        ResponseEntity<Product> responseEntity = this.restTemplate.postForEntity("/api/product", newProduct1(), Product.class);
        assertAll(
                () -> Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode()),
                () -> Assertions.assertEquals(newProduct1(), responseEntity.getBody()));
    }
    @Test
    void addNewProductMissingInput() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Product> entity = new HttpEntity<>(newProductMissingInput(), headers);
        ResponseEntity<Product> responseEntity = this.restTemplate
                .exchange("/api/product", HttpMethod.POST, entity, Product.class, newProductMissingInput());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

//    @Test
//    void deleteProduct() {
//        serviceImplementation.saveProduct(newProductForDelete());
//        HttpHeaders headers = new HttpHeaders();
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        ResponseEntity<String> responseEntity = this.restTemplate.exchange("/api/product/10", HttpMethod.DELETE, entity, String.class);
//        Assertions.assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
//    }

    @Test
    void updateProduct() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Product> entity = new HttpEntity<>(productForUpdate(), headers);
        ResponseEntity<Product> responseEntity = this.restTemplate
                .exchange("/api/product/3", HttpMethod.PUT, entity, Product.class, productForUpdate());
        Assertions.assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    void updateProductIncorrectId() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Product> entity = new HttpEntity<>(productForUpdate(), headers);
        ResponseEntity<Product> responseEntity = this.restTemplate
                .exchange("/api/product/2", HttpMethod.PUT, entity, Product.class, productForUpdate());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    private Product newProduct1() {
        Product product = new Product();
        product.setId(11L);
        product.setName("Wardrobe");
        product.setPrice(BigDecimal.valueOf(357.83));
        product.setQuantity(26);
        product.setDescription("description_wardrobe");
        product.setPhoto("photo_link_wardrobe");
        return product;
    }

    private Product newProductForDelete() {
        Product product = new Product();
        product.setId(7L);
        product.setName("Coffee table");
        product.setPrice(BigDecimal.valueOf(45.65));
        product.setQuantity(31);
        product.setDescription("description_coffeetable");
        product.setPhoto("photo_link_coffeetable");
        return product;
    }

    private Product productForUpdate() {
        Product product = new Product();
        product.setId(3L);
        product.setName("ArmchairUpdated");
        product.setPrice(BigDecimal.valueOf(90.65));
        product.setQuantity(15);
        product.setDescription("description_ArmchairUpdated");
        product.setPhoto("photo_link_ArmchairUpdated");
        return product;
    }

    private Product newProductMissingInput() {
        Product product = new Product();
        product.setId(10L);
        product.setPrice(BigDecimal.valueOf(32.65));
        product.setQuantity(15);
        product.setDescription("description_lamp");
        product.setPhoto("photo_link_lamp");
        return product;
    }

}
