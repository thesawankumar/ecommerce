package thesawan.in.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thesawan.in.ecommerce.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
