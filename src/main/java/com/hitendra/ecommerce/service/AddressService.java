package com.hitendra.ecommerce.service;


import com.hitendra.ecommerce.payload.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO);

    List<AddressDTO> getAllAddress();

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getAllUserAddress();

    AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);


    AddressDTO deleteAddress(Long addressId);
}
