package com.richardpdev.inventoryerp.inventory;

import com.richardpdev.inventoryerp.inventory.dto.CreateMovementRequest;
import com.richardpdev.inventoryerp.inventory.dto.InventoryMovementResponse;
import com.richardpdev.inventoryerp.product.Product;
import com.richardpdev.inventoryerp.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class InventoryMovementService {

    private final ProductRepository productRepo;
    private final InventoryMovementRepository movementRepo;

    public InventoryMovementService(ProductRepository productRepo, InventoryMovementRepository movementRepo) {
        this.productRepo = productRepo;
        this.movementRepo = movementRepo;
    }

    @Transactional
    public InventoryMovementResponse register(CreateMovementRequest req) {
        Product product = productRepo.findById(req.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        int before = product.getStock();
        int after = before;
        int storedQuantity = 0;

        switch (req.type()) {
            case IN -> {
                if (req.quantity() == null || req.quantity() < 1) {
                    throw new IllegalArgumentException("IN movement requires quantity >= 1");
                }
                after = before + req.quantity();
                storedQuantity = req.quantity();
            }
            case OUT -> {
                if (req.quantity() == null || req.quantity() < 1) {
                    throw new IllegalArgumentException("OUT movement requires quantity >= 1");
                }
                if (before < req.quantity()) {
                    throw new IllegalArgumentException("Insufficient stock");
                }
                after = before - req.quantity();
                storedQuantity = req.quantity();
            }
            case ADJUSTMENT -> {
                if (req.quantity() == null || req.quantity() < 0) {
                    throw new IllegalArgumentException("ADJUSTMENT requires quantity >= 0 (new stock level)");
                }
                after = req.quantity();
                storedQuantity = after;
            }
        }

        product.setStock(after);
        productRepo.save(product);

        InventoryMovement movement = new InventoryMovement();
        movement.setProduct(product);
        movement.setMovementType(req.type());
        movement.setQuantity(storedQuantity);
        movement.setStockBefore(before);
        movement.setStockAfter(after);
        movement.setNote(trimNote(req.note()));
        movement.setCreatedAt(Instant.now());

        InventoryMovement saved = movementRepo.save(movement);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<InventoryMovementResponse> listAll() {
        return movementRepo.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InventoryMovementResponse> listByProduct(Long productId) {
        if (!productRepo.existsById(productId)) {
            throw new IllegalArgumentException("Product not found");
        }
        return movementRepo.findByProduct_IdOrderByCreatedAtDesc(productId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public InventoryMovementResponse getById(Long id) {
        InventoryMovement m = movementRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movement not found"));
        return toResponse(m);
    }

    private static String trimNote(String note) {
        if (note == null || note.isBlank()) {
            return null;
        }
        return note.trim();
    }

    private InventoryMovementResponse toResponse(InventoryMovement m) {
        return new InventoryMovementResponse(
                m.getId(),
                m.getProduct().getId(),
                m.getProduct().getSku(),
                m.getMovementType(),
                m.getQuantity(),
                m.getStockBefore(),
                m.getStockAfter(),
                m.getNote(),
                m.getCreatedAt()
        );
    }
}
