package com.hitendra.ecommerce.service;

import com.hitendra.ecommerce.exceptions.APIException;
import com.hitendra.ecommerce.exceptions.ResourceNotFoundException;
import com.hitendra.ecommerce.model.*;
import com.hitendra.ecommerce.payload.OrderDTO;
import com.hitendra.ecommerce.payload.OrderItemDTO;
import com.hitendra.ecommerce.repository.*;
import com.hitendra.ecommerce.utils.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImplementation implements OrderService{

    private final AuthUtil authUtil;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    public OrderServiceImplementation(AuthUtil authUtil, CartRepository cartRepository, AddressRepository addressRepository, PaymentRepository paymentRepository, OrderRepository orderRepository, ModelMapper modelMapper, OrderItemRepository orderItemRepository, ProductRepository productRepository, CartService cartService, ModelMapper modelMapper1) {
        this.authUtil = authUtil;
        this.cartRepository = cartRepository;
        this.addressRepository = addressRepository;
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.modelMapper = modelMapper1;
    }

    @Override
    @Transactional
    public OrderDTO placeOrder(Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {
        String emailId = authUtil.loggedInEmail();

        Cart cart = cartRepository.findCartByEmail(emailId);

        if(cart==null)
            throw new ResourceNotFoundException("Cart", "email", emailId);

        Address address = addressRepository.findById(addressId)
                .orElseThrow( () -> new ResourceNotFoundException("Address", "addressId", addressId));

        Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus, pgResponseMessage, pgName);

        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setOrderPrice(cart.getTotalPrice());
        order.setAddress(address);
        order.setOrderStatus("Order Accepted");

        payment.setOrder(order);
        payment = paymentRepository.save(payment);

        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);

        List<CartItem> cartItems = cart.getCartItems();

        if(cartItems.isEmpty())
            throw new APIException("User doesn't have any product added in cart");

        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }

        orderItems =  orderItemRepository.saveAll(orderItems);

        cartItems.forEach(item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity()-quantity);
            productRepository.save(product);

            cartService.deleteProductFromCart(cart.getCardId(), product.getProductId());
        });


        //Need to resolved as explicitly setting the cart value to 0 after placing the order
        cart.setTotalPrice(0.00);
        cartRepository.save(cart);

        OrderDTO orderDTO =  modelMapper.map(savedOrder, OrderDTO.class);

        orderItems.forEach(orderItem -> {
            orderDTO.getOrderItems().add(modelMapper.map(orderItem, OrderItemDTO.class));
        });

        orderDTO.setAddressId(addressId);

        return orderDTO;
    }
}