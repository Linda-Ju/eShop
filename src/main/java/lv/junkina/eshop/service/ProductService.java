package lv.junkina.eshop.service;

import lv.junkina.eshop.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<Product> findProductById(Long id);

    List<Product> findAllProducts();

    Product saveProduct(Product product);

    void deleteProductById(Long id);
}
