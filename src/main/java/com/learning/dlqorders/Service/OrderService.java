package com.learning.dlqorders.Service;

import com.learning.dlqorders.DTO.DTORequest.OrderRequest;
import com.learning.dlqorders.Entity.Order;
import com.learning.dlqorders.Repository.OrderRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderProducerService orderProducerService;

  @Transactional
  public Order saveOrder(OrderRequest request) {
    validate(request);
    Order order = Order.builder()
        .productName(request.productName().trim())
        .quantity(request.quantity())
        .price(request.price())
        .build();

    // Save first so the published message carries the generated id and createdAt
    Order saved = orderRepository.save(order);
    orderProducerService.publishOrderAsync(saved);
    return saved;
  }

  private void validate(OrderRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("Order request body is required");
    }
    if (request.productName() == null || request.productName().isBlank()) {
      throw new IllegalArgumentException("productName is required");
    }
    if (request.quantity() == null || request.quantity() <= 0) {
      throw new IllegalArgumentException("quantity must be greater than 0");
    }
    if (request.price() == null || request.price().compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("price must be greater than 0");
    }
  }
}
