package com.richardpdev.inventoryerp.inventory.dto;

import com.richardpdev.inventoryerp.inventory.MovementType;

import java.time.Instant;

public record InventoryMovementResponse(
        Long id,
        Long productId,
        String productSku,
        MovementType type,
        Integer quantity,
        Integer stockBefore,
        Integer stockAfter,
        String note,
        Instant createdAt
) {}
