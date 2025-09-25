package com.hitendra.ecommerce.repository;

import com.hitendra.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1")
    Cart findCartByEmail(String email);

    @Query("select c from Cart c where c.user.email=?1 and c.cardId=?2")
    Cart findCartByEmailAndCardId(String email,Long cardId);

    @Query("select c from Cart c join fetch c.cartItems ci join fetch ci.product p where p.productId = ?1")
    List<Cart> findCartByProductId(Long productId);
}
