package thesawan.in.ecommerce.service;

import thesawan.in.ecommerce.model.User;

public interface UserService {
    User findUserByJwtToken(String jwt) throws Exception;

    User findUserByEmail(String email);
}
