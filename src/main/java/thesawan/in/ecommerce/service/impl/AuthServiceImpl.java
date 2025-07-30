package thesawan.in.ecommerce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import thesawan.in.ecommerce.domain.USER_ROLE;
import thesawan.in.ecommerce.model.Cart;
import thesawan.in.ecommerce.model.Seller;
import thesawan.in.ecommerce.model.User;
import thesawan.in.ecommerce.model.VerificationCode;
import thesawan.in.ecommerce.provider.JwtProvider;
import thesawan.in.ecommerce.repository.CartRepository;
import thesawan.in.ecommerce.repository.SellerRepository;
import thesawan.in.ecommerce.repository.UserRepository;
import thesawan.in.ecommerce.repository.VerificationCodeRepository;
import thesawan.in.ecommerce.response.AuthResponse;
import thesawan.in.ecommerce.response.LoginRequest;
import thesawan.in.ecommerce.response.SignUpRequest;
import thesawan.in.ecommerce.service.AuthService;
import thesawan.in.ecommerce.service.EmailService;
import thesawan.in.ecommerce.utils.OtpUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomUserServiceImpl customUserService;

    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public void sentLoginOtp(String email, USER_ROLE role) throws Exception {
        String SIGNING_PREFIX = "signin_";

        if (email.startsWith(SIGNING_PREFIX)) {
            email = email.substring(SIGNING_PREFIX.length());
            if (role.equals(USER_ROLE.ROLE_SELLER)) {
                // For seller, we can add a similar check if needed
                Seller seller = sellerRepository.findByEmail(email);
                if (seller == null) {
                    throw new Exception("Seller not found with this provide email");
                }
            } else {
                User user = userRepository.findByEmail(email);
                if (user == null) {
                    throw new Exception("User not found with this provide email");
                }
            }


        }

        VerificationCode isExist = verificationCodeRepository.findByEmail(email);
        if (isExist != null) {
            verificationCodeRepository.delete(isExist);
        }
        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);


        String subject = "🔐 Your One-Time Password (OTP) for Login Verification";

        String text = String.format("""
                <div style="font-family: Arial, sans-serif; color: #333;">
                    <p>Hello,</p>
                
                    <p>We received a request to log in to your account on <strong>TheSawan E-Commerce App</strong>.</p>
                
                    <p>Please use the following One-Time Password (OTP) to proceed:</p>
                
                    <h2 style="background: #f2f2f2; padding: 10px; border-radius: 5px; display: inline-block; color: #2c3e50;">
                        %s
                    </h2>
                
                    <p style="margin-top: 20px;">🔒 <strong>Note:</strong> This OTP is valid for <strong>10 minutes</strong>. Do not share this code with anyone.</p>
                
                    <p>If you did not initiate this request, you can safely ignore this email.</p>
                
                    <br>
                
                    <p>Thanks & Regards,<br>
                    <strong>TheSawan E-Commerce Team</strong></p>
                </div>
                """, otp);

        emailService.sendVerificationOtpEmail(email, otp, subject, text);
    }


    @Override
    public String createUser(SignUpRequest req) throws Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());
        if (verificationCode == null && !verificationCode.getOtp().equals(req.getOtp())) {
            throw new Exception("WRONG OTP....");
        }
        User user = userRepository.findByEmail(req.getEmail());
        if (user == null) {
            User createdUser = new User();
            createdUser.setEmail(req.getEmail());
            createdUser.setFullName(req.getFullName());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setMobile("9142182916");
            createdUser.setPassword(passwordEncoder.encode(req.getOtp()));
            user = userRepository.save(createdUser);
            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateToken(authentication);
    }

    @Override
    public AuthResponse sign_in(LoginRequest req) throws Exception {

        String username = req.getEmail();
        String otp = req.getOtp();

        Authentication authentication = authenticate(username, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login Success:)");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
        authResponse.setRole(USER_ROLE.valueOf(roleName));
        return authResponse;
    }

    private Authentication authenticate(String username, String otp) throws Exception {

        UserDetails userDetails = customUserService.loadUserByUsername(username);
        String SELLER_PREFIX = "seller_";
        if (username.startsWith(SELLER_PREFIX)) {
            username = username.substring(SELLER_PREFIX.length());
        }
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid UserName or Password");
        }
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("WRONG OTP.....");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,
                null,
                userDetails.getAuthorities());
    }
}
