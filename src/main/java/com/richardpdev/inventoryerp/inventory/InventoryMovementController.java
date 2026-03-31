package com.richardpdev.inventoryerp.inventory;

import com.richardpdev.inventoryerp.inventory.dto.CreateMovementRequest;
import com.richardpdev.inventoryerp.inventory.dto.InventoryMovementResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory-movements")
public class InventoryMovementController {

    private final InventoryMovementService service;

    public InventoryMovementController(InventoryMovementService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryMovementResponse register(@Valid @RequestBody CreateMovementRequest req) {
        return service.register(req);
    }

    @GetMapping
    public List<InventoryMovementResponse> list() {
        return service.listAll();
    }

    @GetMapping("/product/{productId}")
    public List<InventoryMovementResponse> listByProduct(@PathVariable Long productId) {
        return service.listByProduct(productId);
    }

    @GetMapping("/{id}")
    public InventoryMovementResponse get(@PathVariable Long id) {
        return service.getById(id);
    }
}
