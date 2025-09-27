package com.hitendra.ecommerce.controller;

import com.hitendra.ecommerce.model.User;
import com.hitendra.ecommerce.payload.AddressDTO;
import com.hitendra.ecommerce.service.AddressService;
import com.hitendra.ecommerce.utils.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    private final AddressService addressService;
    private final AuthUtil authUtil;

    public AddressController(AddressService addressService, AuthUtil authUtil) {
        this.addressService = addressService;
        this.authUtil = authUtil;
    }

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        User user = authUtil.loggedInUser();
        return new ResponseEntity<>(
                addressService.createAddress(addressDTO, user),
                HttpStatus.CREATED
        );
    }

    @GetMapping("addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddress() {
        return new ResponseEntity<>(addressService.getAllAddress(), HttpStatus.FOUND);
    }

    @GetMapping("addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        return new ResponseEntity<>(addressService.getAddressById(addressId), HttpStatus.FOUND);
    }

    @GetMapping("user/address")
    public ResponseEntity<List<AddressDTO>> getAllUserAddress() {
        return new ResponseEntity<>(addressService.getAllUserAddress(), HttpStatus.FOUND);
    }

    @PutMapping("address/{addressId}")
    public ResponseEntity<AddressDTO> updateUserAddress(
            @PathVariable Long addressId,
            @RequestBody AddressDTO addressDTO
    ) {
        return new ResponseEntity<>(addressService.updateAddress(addressId, addressDTO), HttpStatus.OK);
    }

    @DeleteMapping("address/{addressId}")
    public ResponseEntity<AddressDTO> deleteAddress(@PathVariable Long addressId) {
        return new ResponseEntity<>(addressService.deleteAddress(addressId), HttpStatus.OK);
    }

}
