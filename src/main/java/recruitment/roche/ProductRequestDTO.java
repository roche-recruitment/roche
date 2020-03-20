package recruitment.roche;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductRequestDTO {
    @NotNull
    private String sku;
    @NotNull
    private String name;
    @NotNull
    private BigDecimal price;

    public ProductRequestDTO(String sku, String name, BigDecimal price) {
        this.sku = sku;
        this.name = name;
        this.price = price;
    }

    public ProductRequestDTO() {
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

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
