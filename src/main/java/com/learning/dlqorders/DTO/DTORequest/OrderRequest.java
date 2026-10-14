package com.learning.dlqorders.DTO.DTORequest;

import java.math.BigDecimal;

public record OrderRequest(String productName, Integer quantity, BigDecimal price) {
}
