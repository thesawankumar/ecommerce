package thesawan.in.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thesawan.in.ecommerce.model.Address;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
