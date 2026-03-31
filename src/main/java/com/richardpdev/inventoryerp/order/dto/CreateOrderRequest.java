package com.richardpdev.inventoryerp.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequest(
        @NotEmpty @Valid List<OrderLineItemRequest> lines,
        String notes
) {}
