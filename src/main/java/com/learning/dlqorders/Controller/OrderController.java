package com.learning.dlqorders.Controller;

import com.learning.dlqorders.DTO.DTORequest.OrderRequest;
import com.learning.dlqorders.Entity.Order;
import com.learning.dlqorders.Service.OrderService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<Order> saveOrder(@RequestBody OrderRequest request) {
    Order saved = orderService.saveOrder(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
    return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
  }
}
