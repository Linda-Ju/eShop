package lv.junkina.eShop.mappers;

import lv.junkina.eShop.model.Product;
import lv.junkina.eShop.repository.model.ProductDAO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapStructMapper {
    ProductDAO productToProductDAO(Product product);

    Product productDAOToProduct(ProductDAO productDAO);
}
