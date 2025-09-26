package com.hitendra.ecommerce.controller;

import com.hitendra.ecommerce.payload.OrderDTO;
import com.hitendra.ecommerce.payload.OrderRequestDTO;
import com.hitendra.ecommerce.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(
            @PathVariable String paymentMethod,
            @RequestBody OrderRequestDTO orderRequestDTO
    ) {
        return new ResponseEntity<>(
                orderService.placeOrder(
                        orderRequestDTO.getAddressId(),
                        paymentMethod,
                        orderRequestDTO.getPgName(),
                        orderRequestDTO.getPgPaymentId(),
                        orderRequestDTO.getPgStatus(),
                        orderRequestDTO.getPgResponseMessage()
                ),
                HttpStatus.CREATED
        );
    }
}
