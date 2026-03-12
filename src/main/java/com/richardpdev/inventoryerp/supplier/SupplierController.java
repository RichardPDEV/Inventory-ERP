package com.richardpdev.inventoryerp.supplier;

import com.richardpdev.inventoryerp.supplier.dto.CreateSupplierRequest;
import com.richardpdev.inventoryerp.supplier.dto.SupplierResponse;
import com.richardpdev.inventoryerp.supplier.dto.UpdateSupplierRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
public class SupplierController {

    private final SupplierService service;

    public SupplierController(SupplierService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SupplierResponse create(@Valid @RequestBody CreateSupplierRequest req) {
        return service.create(req);
    }

    @GetMapping
    public List<SupplierResponse> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public SupplierResponse get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public SupplierResponse update(@PathVariable Long id, @Valid @RequestBody UpdateSupplierRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
