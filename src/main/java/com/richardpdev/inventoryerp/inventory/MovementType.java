package com.richardpdev.inventoryerp.inventory;

public enum MovementType {
    /** Stock increase (purchase, return to warehouse). */
    IN,
    /** Stock decrease (sale, damage). Quantity must not exceed current stock. */
    OUT,
    /** Set stock to an absolute value (cycle count, correction). */
    ADJUSTMENT
}
