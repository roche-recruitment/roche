package recruitment.roche;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Test
    @DirtiesContext
    public void shouldAddNewProduct() {
        // given
        ProductRequestDTO dto = new ProductRequestDTO("some-sku", "some-name", BigDecimal.valueOf(20.2));

        //when
        Product newProduct = productService.createNewProduct(dto);

        // then
        Assertions.assertNotNull(newProduct);
        Assertions.assertNotNull(newProduct.getId());
        Assertions.assertEquals("some-name", newProduct.getName());
    }

    @Test
    @DirtiesContext
    public void shouldUpdateProduct() {
        // given
        ProductRequestDTO createDto = new ProductRequestDTO("some-sku", "some-name", BigDecimal.valueOf(20.2));
        productService.createNewProduct(createDto);

        ProductRequestDTO updateDto = new ProductRequestDTO("some-sku", "some-name2", BigDecimal.valueOf(20.2));
        Product newProduct = productService.updateProduct(updateDto);


        // then
        Assertions.assertNotNull(newProduct);
        Assertions.assertNotNull(newProduct.getId());
        Assertions.assertEquals(updateDto.getName(), newProduct.getName());
    }

    @Test
    @DirtiesContext
    public void shouldDeleteProduct() {
        // given
        ProductRequestDTO createDto = new ProductRequestDTO("some-sku", "some-name", BigDecimal.valueOf(20.2));
        productService.createNewProduct(createDto);

        productService.softDeleteProduct("some-sku");
        Product newProduct = productService.getProduct("some-sku");


        // then
        Assertions.assertNull(newProduct);
    }

}