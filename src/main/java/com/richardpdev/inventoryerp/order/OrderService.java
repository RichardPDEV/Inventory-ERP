package com.richardpdev.inventoryerp.order;

import com.richardpdev.inventoryerp.inventory.InventoryMovementService;
import com.richardpdev.inventoryerp.inventory.MovementType;
import com.richardpdev.inventoryerp.inventory.dto.CreateMovementRequest;
import com.richardpdev.inventoryerp.order.dto.*;
import com.richardpdev.inventoryerp.product.Product;
import com.richardpdev.inventoryerp.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final SalesOrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final InventoryMovementService movementService;

    public OrderService(SalesOrderRepository orderRepo, ProductRepository productRepo,
                        InventoryMovementService movementService) {
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.movementService = movementService;
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest req) {
        Instant now = Instant.now();
        SalesOrder order = new SalesOrder();
        order.setOrderNumber(generateOrderNumber());
        order.setStatus(OrderStatus.PENDING);
        order.setNotes(trimNotes(req.notes()));
        order.setCreatedAt(now);
        order.setUpdatedAt(now);

        for (OrderLineItemRequest lineReq : req.lines()) {
            Product product = productRepo.findById(lineReq.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + lineReq.productId()));
            var unitPrice = lineReq.unitPrice() != null ? lineReq.unitPrice() : product.getPrice();
            OrderLine line = new OrderLine();
            line.setProduct(product);
            line.setQuantity(lineReq.quantity());
            line.setUnitPrice(unitPrice);
            order.addLine(line);
        }

        SalesOrder saved = orderRepo.save(order);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepo.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getById(Long id) {
        SalesOrder order = orderRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return toResponse(order);
    }

    @Transactional
    public OrderResponse approve(Long id) {
        SalesOrder order = orderRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Order can only be approved from PENDING status");
        }
        order.setStatus(OrderStatus.APPROVED);
        order.setUpdatedAt(Instant.now());
        return toResponse(orderRepo.save(order));
    }

    @Transactional
    public OrderResponse dispatch(Long id) {
        SalesOrder order = orderRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (order.getStatus() != OrderStatus.APPROVED) {
            throw new IllegalArgumentException("Order can only be dispatched from APPROVED status");
        }
        for (OrderLine line : order.getLines()) {
            movementService.register(new CreateMovementRequest(
                    line.getProduct().getId(),
                    MovementType.OUT,
                    line.getQuantity(),
                    "Dispatch order " + order.getOrderNumber()
            ));
        }
        order.setStatus(OrderStatus.DISPATCHED);
        order.setUpdatedAt(Instant.now());
        return toResponse(orderRepo.save(order));
    }

    @Transactional
    public OrderResponse cancel(Long id) {
        SalesOrder order = orderRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (order.getStatus() == OrderStatus.DISPATCHED) {
            throw new IllegalArgumentException("Cannot cancel a dispatched order");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Order is already cancelled");
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(Instant.now());
        return toResponse(orderRepo.save(order));
    }

    private static String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }

    private static String trimNotes(String notes) {
        if (notes == null || notes.isBlank()) {
            return null;
        }
        return notes.trim();
    }

    private OrderResponse toResponse(SalesOrder order) {
        List<OrderLineResponse> lineDtos = order.getLines().stream()
                .map(line -> new OrderLineResponse(
                        line.getId(),
                        line.getProduct().getId(),
                        line.getProduct().getSku(),
                        line.getProduct().getName(),
                        line.getQuantity(),
                        line.getUnitPrice()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus().name(),
                order.getNotes(),
                lineDtos,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
