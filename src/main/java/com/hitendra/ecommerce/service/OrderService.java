package com.hitendra.ecommerce.service;

import com.hitendra.ecommerce.payload.OrderDTO;
import com.hitendra.ecommerce.payload.OrderRequestDTO;
import jakarta.transaction.Transactional;


public interface OrderService {

    @Transactional
    OrderDTO placeOrder(Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);
}
