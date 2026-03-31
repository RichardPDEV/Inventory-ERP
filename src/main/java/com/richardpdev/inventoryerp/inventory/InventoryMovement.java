package com.richardpdev.inventoryerp.inventory;

import com.richardpdev.inventoryerp.product.Product;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "inventory_movements")
public class InventoryMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 20)
    private MovementType movementType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "stock_before", nullable = false)
    private Integer stockBefore;

    @Column(name = "stock_after", nullable = false)
    private Integer stockAfter;

    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public InventoryMovement() {}

    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public MovementType getMovementType() { return movementType; }
    public Integer getQuantity() { return quantity; }
    public Integer getStockBefore() { return stockBefore; }
    public Integer getStockAfter() { return stockAfter; }
    public String getNote() { return note; }
    public Instant getCreatedAt() { return createdAt; }

    public void setProduct(Product product) { this.product = product; }
    public void setMovementType(MovementType movementType) { this.movementType = movementType; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setStockBefore(Integer stockBefore) { this.stockBefore = stockBefore; }
    public void setStockAfter(Integer stockAfter) { this.stockAfter = stockAfter; }
    public void setNote(String note) { this.note = note; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
