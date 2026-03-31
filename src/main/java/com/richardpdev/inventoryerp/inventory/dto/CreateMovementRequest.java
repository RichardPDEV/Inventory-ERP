package com.richardpdev.inventoryerp.inventory.dto;

import com.richardpdev.inventoryerp.inventory.MovementType;
import jakarta.validation.constraints.NotNull;

public record CreateMovementRequest(
        @NotNull Long productId,
        @NotNull MovementType type,
        /** For IN/OUT: units to add/remove (positive). For ADJUSTMENT: new absolute stock level. */
        @NotNull Integer quantity,
        String note
) {}
