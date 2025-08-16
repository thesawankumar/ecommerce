package thesawan.in.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thesawan.in.ecommerce.model.Address;
import thesawan.in.ecommerce.model.User;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
}
