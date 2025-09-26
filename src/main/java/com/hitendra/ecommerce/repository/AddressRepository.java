package com.hitendra.ecommerce.repository;

import com.hitendra.ecommerce.model.Address;
import com.hitendra.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAddressByUser(User user);

    Optional<Address> findAddressByUserAndAddressId(User user, Long addressId);
}
