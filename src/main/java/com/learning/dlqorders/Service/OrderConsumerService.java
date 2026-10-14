package com.learning.dlqorders.Service;

import com.learning.dlqorders.Entity.Order;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderConsumerService {

  // Short visibility timeout so failed messages are redelivered quickly
  @SqsListener(value = "${app.sqs.queue-name}", messageVisibilitySeconds = "5")
  public void consume(Order order) {
    log.info("Received order id={} product={}", order.getId(), order.getProductName());

    // Simulated failure: any product name containing "FAIL" always throws
    if (order.getProductName().contains("FAIL")) {
      throw new IllegalStateException("Simulated processing failure for order " + order.getId());
    }

    // Returning normally acknowledges the message, so Spring Cloud AWS deletes it
    log.info("Processed order id={}", order.getId());
  }
}
