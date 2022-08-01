package lv.junkina.eshop.service.impl;

import lombok.extern.log4j.Log4j2;
import lv.junkina.eshop.mappers.ProductMapStructMapper;
import lv.junkina.eshop.model.Product;
import lv.junkina.eshop.repository.ProductRepository;
import lv.junkina.eshop.repository.model.ProductDAO;
import lv.junkina.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ProductServiceImplementation implements ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductMapStructMapper productMapper;
    @Override
    public Optional<Product> findProductById(Long id) {
        Optional<Product> productById = productRepository.findById(id)
                .flatMap(product -> Optional.ofNullable(productMapper.productDAOToProduct(product)));
        log.info("Product with id {} is {}", id, productById);
        return productById;
    }

    @Override
    public List<Product> findAllProducts() {
        List<ProductDAO> productDAOList = productRepository.findAll();
        log.info("Get product list. Size is: {}", productDAOList::size);
        return productDAOList.stream().map(productMapper::productDAOToProduct).collect(Collectors.toList());
    }
    @Override
    public Product saveProduct(Product product) {
        ProductDAO productSaved = productRepository.save(productMapper.productToProductDAO(product));
        log.info("New product saved: {}", () -> productSaved);
        return productMapper.productDAOToProduct(productSaved);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
        log.info("Product with id {} is deleted", id);
    }

    public boolean hasNoMatch(Product product) {
        return productRepository.findAll().stream()
                .noneMatch(t -> !t.getId().equals(product.getId()) &&
                        t.getName().equals(product.getName()));
    }
}
