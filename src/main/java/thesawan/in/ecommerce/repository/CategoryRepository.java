package thesawan.in.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thesawan.in.ecommerce.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryId(String categoryId);

}
