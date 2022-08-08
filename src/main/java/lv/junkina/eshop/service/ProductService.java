package lv.junkina.eshop.service;

import lv.junkina.eshop.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<Product> getProductById(Long id);

    List<Product> getAllProducts();

    Product saveProduct(Product product) ;

    void deleteProductById(Long id);
}
