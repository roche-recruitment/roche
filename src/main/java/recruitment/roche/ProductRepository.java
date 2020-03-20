package recruitment.roche;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("SELECT p FROM Product p where p.sku = ?1 and p.active=true")
    Product findActiveBySku(String sku);

    @Query("SELECT p FROM Product p where p.sku = ?1")
    Product findBySku(String sku);

    @Query("SELECT p FROM Product p WHERE p.active=true")
    List<Product> getAllActive();

}
