package thesawan.in.ecommerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thesawan.in.ecommerce.model.Address;
import thesawan.in.ecommerce.model.User;
import thesawan.in.ecommerce.repository.AddressRepository;

import java.util.List;


public interface AddressService {
    Address addAddress(Address address, User user) throws Exception;
    List<Address> getUserAddresses(User user);
    void deleteAddress(Long id, User user) throws Exception;
    Address updateAddress(Long id, Address updatedAddress, User user) throws Exception; // new
}
