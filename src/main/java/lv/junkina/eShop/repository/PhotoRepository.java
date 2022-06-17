package lv.junkina.eShop.repository;

import lv.junkina.eShop.repository.model.PhotoDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoDAO, Long> {
}
