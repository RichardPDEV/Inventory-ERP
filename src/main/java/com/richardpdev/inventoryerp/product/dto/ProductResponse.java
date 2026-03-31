package com.richardpdev.inventoryerp.product.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String sku,
        BigDecimal price,
        Integer stock,
        Long categoryId,
        String categoryName,
        Long supplierId,
        String supplierName
) {}
