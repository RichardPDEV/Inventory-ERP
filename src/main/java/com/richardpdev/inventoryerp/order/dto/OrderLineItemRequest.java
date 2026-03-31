package com.richardpdev.inventoryerp.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderLineItemRequest(
        @NotNull Long productId,
        @NotNull @Min(1) Integer quantity,
        BigDecimal unitPrice
) {}
