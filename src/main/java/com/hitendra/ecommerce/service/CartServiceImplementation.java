package com.hitendra.ecommerce.service;

import com.hitendra.ecommerce.exceptions.APIException;
import com.hitendra.ecommerce.exceptions.ResourceNotFoundException;
import com.hitendra.ecommerce.model.Cart;
import com.hitendra.ecommerce.model.CartItem;
import com.hitendra.ecommerce.model.Product;
import com.hitendra.ecommerce.payload.CartDTO;
import com.hitendra.ecommerce.payload.ProductDTO;
import com.hitendra.ecommerce.repository.CartItemRepository;
import com.hitendra.ecommerce.repository.CartRepository;
import com.hitendra.ecommerce.repository.ProductRepository;
import com.hitendra.ecommerce.utils.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CartServiceImplementation implements CartService{

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;

    private final AuthUtil authUtil;

    public CartServiceImplementation(CartRepository cartRepository, ProductRepository productRepository, CartItemRepository cartItemRepository, ModelMapper modelMapper, AuthUtil authUtil) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.modelMapper = modelMapper;
        this.authUtil = authUtil;
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart!=null)
            return userCart;

        Cart cart = new Cart();
        cart.setUser(authUtil.loggedInUser());
        cart.setTotalPrice(0.00);
        return cartRepository.save(cart);
    }

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {

        Cart userCart = createCart();

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(
                userCart.getCardId(),
                productId
        );

        if(cartItem != null) {
            throw new APIException(product.getProductName()+ " already exists in the cart.");
        }

        if(product.getQuantity()==0) {
            throw new APIException(product.getProductName()+ " is not available.");
        }

        if(product.getQuantity()<quantity) {
            throw new APIException("Please make an order of " + product.getProductName() + " less or equal to quantity: " + product.getQuantity() + ".");
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setCart(userCart);
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);

        userCart.getCartItems().add(newCartItem);

        product.setQuantity(product.getQuantity());

        userCart.setTotalPrice(userCart.getTotalPrice() + (product.getSpecialPrice() * quantity));

        Cart cart = cartRepository.save(userCart);
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productStream = cartItems.stream().map(item->{
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });

        cartDTO.setProducts(productStream.toList());

        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if(carts.isEmpty())
            throw new APIException("No carts available at the moment");
        return carts.stream()
                .map(cart -> {
                    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
                    cart.getCartItems().forEach(
                            c->c.getProduct().setQuantity(c.getQuantity())
                    );
                    List<ProductDTO> productDTOS = cart.getCartItems().stream()
                            .map(product -> modelMapper.map(product.getProduct(), ProductDTO.class))
                            .toList();
                    cartDTO.setProducts(productDTOS);
                    return cartDTO;
                }).toList();
    }

    @Override
    public CartDTO getUserCart() {

        String email = authUtil.loggedInEmail();
        Long cartId = cartRepository.findCartByEmail(email).getCardId();
        Cart cart = cartRepository.findCartByEmailAndCardId(email, cartId);
        if(cart!=null) {
            CartDTO cartDTO =  modelMapper.map(cart, CartDTO.class);
            cart.getCartItems().forEach(
                    c->c.getProduct().setQuantity(c.getQuantity())
            );
            List<ProductDTO> cartItems = cart.getCartItems().stream()
                    .map(item -> modelMapper.map(item.getProduct(), ProductDTO.class))
                    .toList();
            cartDTO.setProducts(cartItems);
            return cartDTO;
        }

        throw new APIException("User doesn't have any existing cart");
    }

    @Override
    @Transactional
    public CartDTO updateUserCart(Long productId, int quantity) {
        Long cartId = cartRepository
                .findCartByEmail(authUtil.loggedInEmail())
                .getCardId();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if(product.getQuantity()==0) {
            throw new APIException(product.getProductName()+ " is not available.");
        }

        if(product.getQuantity()<quantity) {
            throw new APIException("Please make an order of " + product.getProductName() + " less or equal to quantity: " + product.getQuantity() + ".");
        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, cartId);

        if(cartItem==null)
            throw new APIException(product.getProductName() + " doesn't present in user cart.");

        int newQuantity = cartItem.getQuantity() + quantity;

        if(newQuantity < 0) {
            throw new APIException("The resulting quantity can't be zero");
        }

        if(newQuantity==0) {
            deleteProductFromCart(cartId, productId);
        } else {
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));
            cartRepository.save(cart);
        }

        CartItem updatedItem = cartItemRepository.save(cartItem);
        if(cartItem.getQuantity() == 0) {
            cartItemRepository.deleteById(updatedItem.getCartItemId());
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item->{
            ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
            productDTO.setQuantity(item.getQuantity());
            return productDTO;
        });

        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository
                .findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, cartId);

        if(cartItem==null)
            throw new APIException(product.getProductName() + " doesn't exist in user cart.");

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.deleteCartItemByProductIdAndCartId(productId, cartId);

        return cartItem.getProduct().getProductName() + " removed from cart";
    }

    @Override
    public void updateProductsInCarts(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, cartId);
        if(cartItem==null)
            throw new APIException(product.getProductName() + " doesn't exist in cart ");

        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());

        cartItem.setProductPrice(product.getSpecialPrice());

        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.save(cartItem);
    }
}