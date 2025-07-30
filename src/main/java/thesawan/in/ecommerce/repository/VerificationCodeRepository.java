package thesawan.in.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thesawan.in.ecommerce.model.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode,Long> {
    VerificationCode findByEmail(String email);
    VerificationCode findByOtp(String otp);

}
