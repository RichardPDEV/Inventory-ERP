package com.richardpdev.inventoryerp.supplier.dto;

public record SupplierResponse(
        Long id,
        String name,
        String contactEmail,
        String contactPhone
) {}
