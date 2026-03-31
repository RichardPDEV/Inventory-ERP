package com.richardpdev.inventoryerp.order.dto;

import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        String status,
        String notes,
        List<OrderLineResponse> lines,
        Instant createdAt,
        Instant updatedAt
) {}
