package thesawan.in.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thesawan.in.ecommerce.model.HomeCategory;

public interface HomeCategoryRepository extends JpaRepository<HomeCategory, Long> {
}
