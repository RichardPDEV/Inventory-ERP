package com.richardpdev.inventoryerp.supplier;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsByNameIgnoreCase(String name);
}
