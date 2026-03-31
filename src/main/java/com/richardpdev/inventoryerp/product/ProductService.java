package com.richardpdev.inventoryerp.product;

import com.richardpdev.inventoryerp.category.Category;
import com.richardpdev.inventoryerp.category.CategoryRepository;
import com.richardpdev.inventoryerp.product.dto.CreateProductRequest;
import com.richardpdev.inventoryerp.product.dto.ProductResponse;
import com.richardpdev.inventoryerp.product.dto.UpdateProductRequest;
import com.richardpdev.inventoryerp.supplier.Supplier;
import com.richardpdev.inventoryerp.supplier.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final SupplierRepository supplierRepo;

    public ProductService(ProductRepository productRepo, CategoryRepository categoryRepo, SupplierRepository supplierRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.supplierRepo = supplierRepo;
    }

    @Transactional
    public ProductResponse create(CreateProductRequest req) {
        if (productRepo.existsBySkuIgnoreCase(req.sku().trim())) {
            throw new IllegalArgumentException("Product SKU already exists");
        }
        Category category = categoryRepo.findById(req.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        Supplier supplier = supplierRepo.findById(req.supplierId())
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));

        Product product = new Product();
        product.setName(req.name().trim());
        product.setSku(req.sku().trim().toUpperCase());
        product.setPrice(req.price());
        product.setStock(req.stock() != null ? req.stock() : 0);
        product.setCategory(category);
        product.setSupplier(supplier);

        Product saved = productRepo.save(product);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return toResponse(p);
    }

    @Transactional
    public ProductResponse update(Long id, UpdateProductRequest req) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        String newSku = req.sku().trim().toUpperCase();
        if (!p.getSku().equalsIgnoreCase(newSku) && productRepo.existsBySkuIgnoreCase(newSku)) {
            throw new IllegalArgumentException("Product SKU already exists");
        }

        Category category = categoryRepo.findById(req.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        Supplier supplier = supplierRepo.findById(req.supplierId())
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));

        p.setName(req.name().trim());
        p.setSku(newSku);
        p.setPrice(req.price());
        p.setStock(req.stock());
        p.setCategory(category);
        p.setSupplier(supplier);

        return toResponse(productRepo.save(p));
    }

    @Transactional
    public void delete(Long id) {
        if (!productRepo.existsById(id)) {
            throw new IllegalArgumentException("Product not found");
        }
        productRepo.deleteById(id);
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getSku(),
                p.getPrice(),
                p.getStock(),
                p.getCategory().getId(),
                p.getCategory().getName(),
                p.getSupplier().getId(),
                p.getSupplier().getName()
        );
    }
}
