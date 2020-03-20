package recruitment.roche;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductResponseDTO {
    @NotNull
    private String sku;
    @NotNull
    private String name;
    @NotNull
    private BigDecimal price;
    @NotNull
    private long createdAt;


    public ProductResponseDTO(String sku, String name, BigDecimal price, long createdAt) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.createdAt = createdAt;
    }

    public ProductResponseDTO() {
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public static ProductResponseDTO from(Product product) {
        return new ProductResponseDTO(product.getSku(), product.getName(), product.getPrice(), product.getCreatedAt());
    }

}
