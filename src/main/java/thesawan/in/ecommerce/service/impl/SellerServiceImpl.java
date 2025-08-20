package thesawan.in.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import thesawan.in.ecommerce.domain.AccountStatus;
import thesawan.in.ecommerce.domain.USER_ROLE;
import thesawan.in.ecommerce.exceptions.SellerException;
import thesawan.in.ecommerce.model.Address;
import thesawan.in.ecommerce.model.Seller;
import thesawan.in.ecommerce.provider.JwtProvider;
import thesawan.in.ecommerce.repository.AddressRepository;
import thesawan.in.ecommerce.repository.SellerRepository;
import thesawan.in.ecommerce.service.SellerService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;


    @Override
    public Seller getSellerProfile(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return this.getSellerByEmail(email);
    }

//    @Override
//    public Seller createSellerProfile(Seller seller) throws Exception {
//        Seller sellerIsExist = sellerRepository.findByEmail(seller.getEmail());
//        if (sellerIsExist != null) {
//            throw new Exception("Seller already exists with email: " + seller.getEmail());
//        }
//
//        Address savedAddress = addressRepository.save(seller.getPickupAddress());
//        Seller newSeller = new Seller();
//        newSeller.setEmail(seller.getEmail());
//        newSeller.setPassword(seller.getPassword());
//        newSeller.setSellerName(seller.getSellerName());
//        newSeller.setPickupAddress(savedAddress);
//        newSeller.setGSTIN(seller.getGSTIN());

    /// /        System.out.println(seller.getRole());
//        newSeller.setRole(USER_ROLE.ROLE_SELLER);
//        newSeller.setMobile(seller.getMobile());
//        newSeller.setBankDetails(seller.getBankDetails());
//        newSeller.setBusinessDetails(seller.getBusinessDetails());
//
//        return sellerRepository.save(newSeller);
//    }
    @Override
    public Seller createSellerProfile(Seller seller) throws Exception {
        Seller sellerIsExist = sellerRepository.findByEmail(seller.getEmail());
        if (sellerIsExist != null) {
            throw new Exception("Seller already exists with email: " + seller.getEmail());
        }

        Seller newSeller = new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(seller.getPassword());
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickupAddress(seller.getPickupAddress()); // cascade will save
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setMobile(seller.getMobile());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());

        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws SellerException {
        return sellerRepository.findById(id).orElseThrow(
                () -> new SellerException("Seller not found with id: " + id));
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {
        Seller seller = sellerRepository.findByEmail(email);
        if (seller == null) {
            throw new Exception("Seller not found with email: " + email);
        }
        return seller;
    }


    @Override
    public List<Seller> getAllSellers(AccountStatus status) {
        if (status == null) {
            return sellerRepository.findAll();
        }
        // Filter by enum properly
        return sellerRepository.findAll().stream()
                .filter(s -> s.getAccountStatus() == status)
                .toList();
    }

    @Override
    public Seller updateSellerProfile(Long id, Seller seller) throws Exception {
        Seller existingSeller = this.getSellerById(id);

        if (seller.getSellerName() != null) {
            existingSeller.setSellerName(seller.getSellerName());
        }
        if (seller.getMobile() != null) {
            existingSeller.setMobile(seller.getMobile());
        }
        if (seller.getEmail() != null) {
            existingSeller.setEmail(seller.getEmail());
        }
        if (seller.getPickupAddress() != null && seller.getPickupAddress().getAddress() != null
                && seller.getPickupAddress().getCity() != null
                && seller.getPickupAddress().getState() != null
                && seller.getPickupAddress().getMobile() != null) {
            existingSeller.getPickupAddress().setAddress(seller.getPickupAddress().getAddress());
            existingSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
            existingSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
            existingSeller.getPickupAddress().setPinCode(seller.getPickupAddress().getPinCode());
            existingSeller.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());

        }
        if (seller.getGSTIN() != null) {
            existingSeller.setGSTIN(seller.getGSTIN());
        }
        if (seller.getBankDetails() != null && seller.getBankDetails().getAccountNumber() != null
                && seller.getBankDetails().getIfscCode() != null
                && seller.getBankDetails().getAccountHolderName() != null) {
            existingSeller.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
            existingSeller.getBankDetails().setIfscCode(seller.getBankDetails().getIfscCode());
            existingSeller.getBankDetails().setAccountHolderName(seller.getBankDetails().getAccountHolderName());
        }
        if (seller.getBusinessDetails() != null && seller.getBusinessDetails().getBusinessName() != null) {
            existingSeller.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());
        }
        return sellerRepository.save(existingSeller);
    }

    @Override
    public void deleteSellerProfile(Long id) throws Exception {

        Seller seller = getSellerById(id);
        sellerRepository.delete(seller);
    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {
        Seller seller = getSellerByEmail(email);
        seller.setEmailVerified(true);
        return sellerRepository.save(seller);

    }


    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception {
        Seller seller = getSellerById(sellerId);
        seller.setAccountStatus(status);
        return sellerRepository.save(seller);
    }
}
