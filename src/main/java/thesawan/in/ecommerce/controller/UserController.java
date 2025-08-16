package thesawan.in.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thesawan.in.ecommerce.model.Address;
import thesawan.in.ecommerce.model.User;
import thesawan.in.ecommerce.service.AddressService;
import thesawan.in.ecommerce.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AddressService addressService;

    @GetMapping("/profile")
    public ResponseEntity<User> createUserHandler(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(user);

    }

    @GetMapping("/address/all")
    public ResponseEntity<List<Address>> getUserAddresses(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Address> addresses = addressService.getUserAddresses(user);
        return ResponseEntity.ok(addresses);
    }

    @PostMapping("/address/create")
    public ResponseEntity<Address> addAddress(@RequestHeader("Authorization") String jwt,
                                              @RequestBody Address address) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Address savedAddress = addressService.addAddress(address, user);
        return ResponseEntity.ok(savedAddress);
    }

    // ✅ Update existing address
    @PutMapping("/address/{id}")
    public ResponseEntity<Address> updateAddress(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id,
            @RequestBody Address updatedAddress) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Address address = addressService.updateAddress(id, updatedAddress, user);
        return ResponseEntity.ok(address);
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<String> deleteAddress(@RequestHeader("Authorization") String jwt,
                                                @PathVariable Long id) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        addressService.deleteAddress(id, user);
        return ResponseEntity.ok("Address deleted successfully!");
    }


}
