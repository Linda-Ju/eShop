package lv.junkina.eshop.repository;

import lv.junkina.eshop.repository.model.ProductDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductDAO, Long> {
}
