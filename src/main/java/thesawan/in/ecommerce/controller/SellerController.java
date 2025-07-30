package thesawan.in.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thesawan.in.ecommerce.domain.AccountStatus;
import thesawan.in.ecommerce.exceptions.SellerException;
import thesawan.in.ecommerce.model.Seller;
import thesawan.in.ecommerce.model.VerificationCode;
import thesawan.in.ecommerce.provider.JwtProvider;
import thesawan.in.ecommerce.repository.VerificationCodeRepository;
import thesawan.in.ecommerce.response.ApiResponse;
import thesawan.in.ecommerce.response.AuthResponse;
import thesawan.in.ecommerce.response.LoginRequest;
import thesawan.in.ecommerce.service.AuthService;
import thesawan.in.ecommerce.service.EmailService;
import thesawan.in.ecommerce.service.SellerService;
import thesawan.in.ecommerce.utils.OtpUtil;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    private SellerService sellerService;
    @Autowired
    private VerificationCodeRepository verificationCodeRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtProvider jwtProvider;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception {
        String otp = req.getOtp();
        String email = req.getEmail();
        req.setEmail("seller_" + email); // Prefixing email for sign-in
        AuthResponse authResponse = authService.sign_in(req);
        return ResponseEntity.ok(authResponse);
    }

    @PatchMapping("/verify-email/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception {
        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);
        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("WRONG OTP");
        }
        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);
        return new ResponseEntity<>(seller, HttpStatus.OK);

    }

    @PostMapping("/create-seller")
    public ResponseEntity<Seller> createSellerProfile( @RequestBody Seller seller) throws Exception {
        Seller savedSeller = sellerService.createSellerProfile(seller);
        String otp = OtpUtil.generateOtp();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);

        String subject = "🔐 OTP for Seller Login Verification – TheSawan E-Commerce";

        String text = String.format("""
        <div style="font-family: Arial, sans-serif; color: #333;">
            <p>Hello Seller,</p>

            <p>We received a request to log in to your <strong>Seller Account</strong> on <strong>TheSawan E-Commerce App</strong>.</p>

            <p>Please use the following One-Time Password (OTP) to proceed with the seller login:</p>

            <h2 style="background: #f2f2f2; padding: 10px; border-radius: 5px; display: inline-block; color: #2c3e50;">
                %s
            </h2>

            <p style="margin-top: 20px;">🔒 <strong>Note:</strong> This OTP is valid for <strong>10 minutes</strong>. Please do not share this code with anyone, even if they claim to be from our team.</p>

            <p>If you did not initiate this login request, you can safely ignore this email.</p>

            <br>

            <p>Thanks & Regards,<br>
            <strong>TheSawan E-Commerce Team</strong></p>
        </div>
        """, otp);

        String frontend_url = "http://localhost:3000/seller/verify-email/";
        emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject, text + frontend_url);
        return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException {
        Seller seller = sellerService.getSellerById(id);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        Seller seller = sellerService.getSellerByEmail(email);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @GetMapping("/sellers")
    public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false) AccountStatus status) {
        List<Seller> sellers = sellerService.getAllSellers(status);
        return new ResponseEntity<>(sellers, HttpStatus.OK);
    }

    @PatchMapping("/update-profile")
    public ResponseEntity<Seller> updateSellerProfile(@RequestHeader("Authorization") String jwt, @RequestBody Seller seller) throws Exception {
        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updatedSeller = sellerService.updateSellerProfile(profile.getId(), seller);
        return new ResponseEntity<>(updatedSeller, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Seller> deleteSellerProfile(@PathVariable Long id) throws Exception {
        sellerService.deleteSellerProfile(id);
        return ResponseEntity.noContent().build();
    }
}

