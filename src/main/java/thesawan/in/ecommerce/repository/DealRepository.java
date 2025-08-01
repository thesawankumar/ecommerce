package thesawan.in.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thesawan.in.ecommerce.model.Deal;

public interface DealRepository extends JpaRepository<Deal,Long> {
}
