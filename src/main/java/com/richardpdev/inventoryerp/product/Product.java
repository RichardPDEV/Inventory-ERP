package com.richardpdev.inventoryerp.product;

import com.richardpdev.inventoryerp.category.Category;
import com.richardpdev.inventoryerp.supplier.Supplier;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "sku", nullable = false, unique = true, length = 80)
    private String sku;

    @Column(name = "price", nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    public Product() {}

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSku() { return sku; }
    public BigDecimal getPrice() { return price; }
    public Integer getStock() { return stock; }
    public Category getCategory() { return category; }
    public Supplier getSupplier() { return supplier; }

    public void setName(String name) { this.name = name; }
    public void setSku(String sku) { this.sku = sku; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setStock(Integer stock) { this.stock = stock; }
    public void setCategory(Category category) { this.category = category; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }
}
