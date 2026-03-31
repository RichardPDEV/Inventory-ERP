package com.richardpdev.inventoryerp.order.dto;

import java.math.BigDecimal;

public record OrderLineResponse(
        Long id,
        Long productId,
        String productSku,
        String productName,
        Integer quantity,
        BigDecimal unitPrice
) {}
