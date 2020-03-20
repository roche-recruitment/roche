package recruitment.roche;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;

@Service
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product createNewProduct(ProductRequestDTO productRequestDTO) {

        Product product;
        Product bySku = productRepository.findBySku(productRequestDTO.getSku());
        if (bySku != null && !bySku.isActive()) {
            product = bySku;
        } else {
            product = new Product();
        }

        product.setName(productRequestDTO.getName());
        product.setSku(productRequestDTO.getSku());
        product.setPrice(productRequestDTO.getPrice());
        product.setCreatedAt(Calendar.getInstance().getTimeInMillis());
        product.setActive(true);

        return productRepository.save(product);
    }

    @Transactional
    public Product getProduct(String sku) {
        return productRepository.findActiveBySku(sku);
    }

    @Transactional
    public Product updateProduct(ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findActiveBySku(productRequestDTO.getSku());

        product.setName(productRequestDTO.getName());
        product.setPrice(productRequestDTO.getPrice());

        return productRepository.save(product);
    }

    @Transactional
    public List<Product> getActiveProducts() {
        return productRepository.getAllActive();
    }

    @Transactional
    public void softDeleteProduct(String sku) {
        Product product = productRepository.findActiveBySku(sku);

        product.setActive(false);

        productRepository.save(product);
    }
}
