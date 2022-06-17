package lv.junkina.eShop.service;

import lv.junkina.eShop.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<Product> findProductById(Long id);

    List<Product> findAllProducts();

    Product saveProduct(Product product) throws Exception;

    void deleteProductById(Long id);
}
