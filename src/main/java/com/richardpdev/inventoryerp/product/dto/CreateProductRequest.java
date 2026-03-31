package com.richardpdev.inventoryerp.product.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank @Size(max = 200) String name,
        @NotBlank @Size(max = 80) String sku,
        @NotNull @DecimalMin(value = "0", inclusive = true) BigDecimal price,
        @NotNull @Min(0) Integer stock,
        @NotNull Long categoryId,
        @NotNull Long supplierId
) {}
