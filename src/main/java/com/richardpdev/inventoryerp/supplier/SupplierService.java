package com.richardpdev.inventoryerp.supplier;

import com.richardpdev.inventoryerp.supplier.dto.CreateSupplierRequest;
import com.richardpdev.inventoryerp.supplier.dto.SupplierResponse;
import com.richardpdev.inventoryerp.supplier.dto.UpdateSupplierRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository repo;

    public SupplierService(SupplierRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public SupplierResponse create(CreateSupplierRequest req) {
        if (repo.existsByNameIgnoreCase(req.name().trim())) {
            throw new IllegalArgumentException("Supplier name already exists");
        }
        Supplier saved = repo.save(new Supplier(
                req.name().trim(),
                req.contactEmail(),
                req.contactPhone()
        ));
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<SupplierResponse> list() {
        return repo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SupplierResponse getById(Long id) {
        Supplier s = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));
        return toResponse(s);
    }

    @Transactional
    public SupplierResponse update(Long id, UpdateSupplierRequest req) {
        Supplier s = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));

        String newName = req.name().trim();
        if (!s.getName().equalsIgnoreCase(newName) && repo.existsByNameIgnoreCase(newName)) {
            throw new IllegalArgumentException("Supplier name already exists");
        }

        s.setName(newName);
        s.setContactEmail(req.contactEmail());
        s.setContactPhone(req.contactPhone());
        return toResponse(s);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Supplier not found");
        }
        repo.deleteById(id);
    }

    private SupplierResponse toResponse(Supplier s) {
        return new SupplierResponse(
                s.getId(),
                s.getName(),
                s.getContactEmail(),
                s.getContactPhone()
        );
    }
}
