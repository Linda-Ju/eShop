package lv.junkina.eShop.service.impl;

import lombok.extern.log4j.Log4j2;
import lv.junkina.eShop.mappers.ProductMapStructMapper;
import lv.junkina.eShop.model.Product;
import lv.junkina.eShop.repository.ProductRepository;
import lv.junkina.eShop.repository.model.ProductDAO;
import lv.junkina.eShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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
    public Product saveProduct(Product product) throws Exception {
        if (!hasNoMatch(product)) {
            log.error("Product conflict exception is thrown: {}", HttpStatus.CONFLICT);
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
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
