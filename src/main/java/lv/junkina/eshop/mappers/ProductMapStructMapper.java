package lv.junkina.eshop.mappers;

import lv.junkina.eshop.model.Product;
import lv.junkina.eshop.repository.model.ProductDAO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapStructMapper {
    ProductDAO productToProductDAO(Product product);

    Product productDAOToProduct(ProductDAO productDAO);
}
