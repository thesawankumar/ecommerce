package thesawan.in.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thesawan.in.ecommerce.domain.AccountStatus;
import thesawan.in.ecommerce.model.Address;
import thesawan.in.ecommerce.model.Seller;

import java.util.List;


public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findByEmail(String email);
    List<Seller> findAllByAccountStatus(AccountStatus status);
    List<Seller> findByPickupAddress(Address address);
}
