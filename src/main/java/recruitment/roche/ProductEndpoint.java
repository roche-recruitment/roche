package recruitment.roche;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
@Controller
@RequestMapping(path = "/products", consumes = "application/json", produces = "application/json")
public class ProductEndpoint {

    private ProductService productService;

    public ProductEndpoint(ProductService productService) {
        this.productService = productService;
    }

    @Transactional
    @PostMapping
    public ResponseEntity createNewProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        Product product = productService.getProduct(productRequestDTO.getSku());
        if (product != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("Product with sku :%s already exists.", productRequestDTO.getSku()));
        }

        Product newProduct = productService.createNewProduct(productRequestDTO);

        return ResponseEntity.ok(ProductResponseDTO.from(newProduct));
    }

    @Transactional
    @PutMapping
    public ResponseEntity updateProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        Product product = productService.getProduct(productRequestDTO.getSku());
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Product with sku: %s does not exist.", productRequestDTO.getSku()));
        }

        Product newProduct = productService.updateProduct(productRequestDTO);

        return ResponseEntity.ok(ProductResponseDTO.from(newProduct));
    }

    @Transactional
    @GetMapping
    public ResponseEntity getList() {
        List<Product> activeProducts = productService.getActiveProducts();

        return ResponseEntity.ok(activeProducts.stream().map(ProductResponseDTO::from).collect(Collectors.toList()));
    }

    @Transactional
    @DeleteMapping(path = "/{sku}")
    public ResponseEntity softDeleteProduct(@PathVariable("sku") String sku) {
        Product product = productService.getProduct(sku);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Product with sku: %s does not exists.", sku));
        }

        productService.softDeleteProduct(sku);

        return ResponseEntity.ok().build();
    }

    @Transactional
    @GetMapping(path = "/{sku}")
    public ResponseEntity getProduct(@PathVariable("sku") String sku) {
        Product product = productService.getProduct(sku);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Product with sku: %s does not exists.", sku));
        }

        return ResponseEntity.ok(ProductResponseDTO.from(product));

    }
}
