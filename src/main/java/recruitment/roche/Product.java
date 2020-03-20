package recruitment.roche;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "sku")}, indexes = {@Index(columnList = "active"), @Index(columnList = "sku")})
public class Product {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private String sku;
    @NotNull
    private String name;
    @NotNull
    private BigDecimal price;
    @NotNull
    private long createdAt;
    @JsonIgnore
    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Product(String sku, String name, BigDecimal price, long createdAt, boolean active) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.createdAt = createdAt;
        this.active = active;
    }

    public Product() {
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

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return createdAt == product.createdAt &&
                active == product.active &&
                Objects.equals(id, product.id) &&
                Objects.equals(sku, product.sku) &&
                Objects.equals(name, product.name) &&
                Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sku, name, price, createdAt, active);
    }
}
