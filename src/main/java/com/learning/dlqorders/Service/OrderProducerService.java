package com.learning.dlqorders.Service;

import com.learning.dlqorders.Entity.Order;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class OrderProducerService {

  private final SqsTemplate sqsTemplate;
  private final String queueName;

  public OrderProducerService(SqsTemplate sqsTemplate, @Value("${app.sqs.queue-name}") String queueName) {
    this.sqsTemplate = sqsTemplate;
    this.queueName = queueName;
  }


  public void publishOrderAsync(Order order) {
    // Send asynchronously to SQS
    CompletableFuture<io.awspring.cloud.sqs.operations.SendResult<Order>> future =
        sqsTemplate.sendAsync(queueName, order);

    // Handle success or failure callbacks without blocking the main execution thread
    future.whenComplete((result, exception) -> {
      if (exception != null) {
        log.error("Failed to publish order to SQS: {}", exception.getMessage());
      } else {
        log.info("Successfully published order to SQS with Message ID: {}", result.messageId());
      }
    });
  }
}

