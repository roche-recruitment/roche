package recruitment.roche;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@DirtiesContext
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    public void shouldAddNewProduct() {
        // given
        Product product = new Product("some-unique-id1", "some-name", BigDecimal.valueOf(3.33), 11, true);

        // when
        productRepository.save(product);

        // then
        Product byUuid = productRepository.findActiveBySku("some-unique-id1");
        Assertions.assertEquals("some-name", byUuid.getName());
        Assertions.assertNotNull(byUuid.getId());
    }

    @Test
    public void shouldThrowExceptionWhenAddingProductWithTheSameUuid() {
        // given
        Product product1 = new Product("some-unique-id2", "some-name1", BigDecimal.valueOf(3.33), 11, true);
        Product product2 = new Product("some-unique-id2", "some-name2", BigDecimal.valueOf(4.33), 11, true);

        // when
        productRepository.save(product1);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> productRepository.save(product2));

    }

    @Test
    public void shouldMarkProductAsDeleted() {
        //given
        Product product = new Product("some-unique-id3", "some-name1", BigDecimal.valueOf(3.33), 11, true);
        productRepository.save(product);

        //when
        Product byUuid = productRepository.findActiveBySku("some-unique-id3");
        byUuid.setActive(false);
        productRepository.save(byUuid);

        // then
        Assertions.assertNull(productRepository.findActiveBySku("some-unique-id3"));
    }

    @Test
    public void shouldFilterOutDeletedProduct() {
        //given
        Product product = new Product("some-unique-id4", "some-name1", BigDecimal.valueOf(3.33), 11, false);
        productRepository.save(product);

        //when
        List<Product> actives = productRepository.getAllActive();

        // then
        Assertions.assertNull(actives.stream().filter(product1 -> product1.getName().equals("some-unique-id4")).findAny().orElse(null));
    }


    @Test
    public void shouldReturnNullWhenSearchingForNonExistingSku() {
        //when
        Product product = productRepository.findActiveBySku("non-exising-one");

        // then
        Assertions.assertNull(product);
    }

}
