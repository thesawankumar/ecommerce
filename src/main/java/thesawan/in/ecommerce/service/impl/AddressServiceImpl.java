package thesawan.in.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thesawan.in.ecommerce.model.Address;
import thesawan.in.ecommerce.model.Seller;
import thesawan.in.ecommerce.model.User;
import thesawan.in.ecommerce.repository.AddressRepository;
import thesawan.in.ecommerce.repository.SellerRepository;
import thesawan.in.ecommerce.service.AddressService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {


    private final AddressRepository addressRepository;
    private final SellerRepository sellerRepository;

    @Override
    public Address addAddress(Address address, User user) throws Exception {
        address.setUser(user);
        return addressRepository.save(address);
    }

    @Override
    public List<Address> getUserAddresses(User user) {
        return addressRepository.findByUser(user);
    }

    @Override
    public void deleteAddress(Long id, User user) throws Exception {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new Exception("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new Exception("Unauthorized action");
        }

        // Step 1: Sellers linked to this address
        List<Seller> sellers = sellerRepository.findByPickupAddress(address);
        for (Seller seller : sellers) {
            seller.setPickupAddress(null); // remove reference
            sellerRepository.save(seller);
        }

        // Step 2: Now delete the address
        addressRepository.delete(address);
    }


    @Override
    public Address updateAddress(Long id, Address updatedAddress, User user) throws Exception {
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new Exception("Address not found"));

        if (!existingAddress.getUser().getId().equals(user.getId())) {
            throw new Exception("Unauthorized action");
        }

        // Update fields
        existingAddress.setName(updatedAddress.getName());
        existingAddress.setLocality(updatedAddress.getLocality());
        existingAddress.setCity(updatedAddress.getCity());
        existingAddress.setAddress(updatedAddress.getAddress());
        existingAddress.setState(updatedAddress.getState());
        existingAddress.setPinCode(updatedAddress.getPinCode());
        existingAddress.setMobile(updatedAddress.getMobile());

        return addressRepository.save(existingAddress);
    }
}
