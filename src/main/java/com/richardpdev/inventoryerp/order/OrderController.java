package com.richardpdev.inventoryerp.order;

import com.richardpdev.inventoryerp.order.dto.CreateOrderRequest;
import com.richardpdev.inventoryerp.order.dto.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody CreateOrderRequest req) {
        return orderService.create(req);
    }

    @GetMapping
    public List<OrderResponse> list() {
        return orderService.list();
    }

    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable Long id) {
        return orderService.getById(id);
    }

    @PostMapping("/{id}/approve")
    public OrderResponse approve(@PathVariable Long id) {
        return orderService.approve(id);
    }

    @PostMapping("/{id}/dispatch")
    public OrderResponse dispatch(@PathVariable Long id) {
        return orderService.dispatch(id);
    }

    @PostMapping("/{id}/cancel")
    public OrderResponse cancel(@PathVariable Long id) {
        return orderService.cancel(id);
    }
}
