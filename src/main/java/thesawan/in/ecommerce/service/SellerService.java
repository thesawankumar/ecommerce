package thesawan.in.ecommerce.service;

import org.springframework.stereotype.Service;
import thesawan.in.ecommerce.domain.AccountStatus;
import thesawan.in.ecommerce.model.Seller;

import java.util.List;

public interface SellerService {
    Seller getSellerProfile(String jwt) throws Exception;

    Seller createSellerProfile(Seller seller) throws Exception;

    Seller getSellerById(Long id) throws Exception;

    Seller getSellerByEmail(String email) throws Exception;

    List<Seller> getAllSellers(AccountStatus status);

    Seller updateSellerProfile(Long id, Seller seller) throws Exception;

    void deleteSellerProfile(Long id) throws Exception;

    Seller verifyEmail(String email, String otp) throws Exception;

    Seller updateSellerAccountStatus(Long id, AccountStatus status) throws Exception;

}
