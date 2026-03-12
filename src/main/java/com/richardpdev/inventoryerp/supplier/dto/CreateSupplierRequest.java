package com.richardpdev.inventoryerp.supplier.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSupplierRequest(
        @NotBlank @Size(max = 120) String name,
        @Email @Size(max = 255) String contactEmail,
        @Size(max = 50) String contactPhone
) {
    public CreateSupplierRequest {
        contactEmail = contactEmail != null && contactEmail.isBlank() ? null : contactEmail;
        contactPhone = contactPhone != null && contactPhone.isBlank() ? null : contactPhone;
    }
}
