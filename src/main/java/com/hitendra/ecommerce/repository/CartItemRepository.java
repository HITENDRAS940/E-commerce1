package com.hitendra.ecommerce.repository;

import com.hitendra.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("select ci from CartItem ci where ci.cart.cardId=?2 and ci.product.productId=?1")
    CartItem findCartItemByProductIdAndCartId(Long productId, Long cartId);

    @Modifying
    @Query("delete from CartItem ci where ci.product.productId=?1 and ci.cart.cardId=?2")
    void deleteCartItemByProductIdAndCartId(Long productId, Long cartId);
}