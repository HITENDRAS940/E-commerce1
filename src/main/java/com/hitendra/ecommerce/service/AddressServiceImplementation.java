package com.hitendra.ecommerce.service;

import com.hitendra.ecommerce.exceptions.APIException;
import com.hitendra.ecommerce.exceptions.ResourceNotFoundException;
import com.hitendra.ecommerce.model.Address;
import com.hitendra.ecommerce.model.User;
import com.hitendra.ecommerce.payload.AddressDTO;
import com.hitendra.ecommerce.repository.AddressRepository;
import com.hitendra.ecommerce.repository.UserRepository;
import com.hitendra.ecommerce.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImplementation implements AddressService {

    private final ModelMapper modelMapper;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    public AddressServiceImplementation(ModelMapper modelMapper, AddressRepository addressRepository, UserRepository userRepository, AuthUtil authUtil) {
        this.modelMapper = modelMapper;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.authUtil = authUtil;
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        User user = authUtil.loggedInUser();

        Address address = modelMapper.map(addressDTO, Address.class);
        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);

        Address savedAddress = addressRepository.save(address);

        return modelMapper.map(savedAddress, AddressDTO.class);

    }

    @Override
    public List<AddressDTO> getAllAddress() {
        List<Address> addressList = addressRepository.findAll();
        if(addressList.isEmpty())
            throw new APIException("No Addresses present.");

        return addressList.stream()
                .map(
                        address -> modelMapper.map(address, AddressDTO.class)
                ).toList();
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllUserAddress() {
        User user = authUtil.loggedInUser();

        List<Address> userAddress = addressRepository.findAddressByUser(user);
        if(userAddress.isEmpty())
            throw new APIException("User doesn't have any saved addresses");
        
        return userAddress.stream().map(address ->
                    modelMapper.map(address, AddressDTO.class)
                ).toList();
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {

        Address savedAddress = addressRepository.findAddressByUserAndAddressId(authUtil.loggedInUser(), addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        savedAddress.setStreet(addressDTO.getStreet());
        savedAddress.setBuildingName(addressDTO.getBuildingName());
        savedAddress.setCity(addressDTO.getCity());
        savedAddress.setState(addressDTO.getState());
        savedAddress.setCountry(addressDTO.getCountry());
        savedAddress.setPincode(addressDTO.getPincode());

       Address updatedAddress = addressRepository.save(savedAddress);

       User user = savedAddress.getUser();
       user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
       user.getAddresses().add(updatedAddress);
        userRepository.save(user);

        return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    public AddressDTO deleteAddress(Long addressId) {
        Address savedAddress = addressRepository.findAddressByUserAndAddressId(authUtil.loggedInUser(), addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressID", addressId));

        addressRepository.delete(savedAddress);

        User user = savedAddress.getUser();

        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));

        return modelMapper.map(savedAddress, AddressDTO.class);
    }
}
