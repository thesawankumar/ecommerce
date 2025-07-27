package thesawan.in.ecommerce.response;

import lombok.Data;
import thesawan.in.ecommerce.domain.USER_ROLE;

@Data
public class AuthResponse {
    private String jwt;
    private String message;
    private USER_ROLE role;

}
