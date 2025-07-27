package thesawan.in.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thesawan.in.ecommerce.model.Seller;


public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findByEmail(String email);
}
