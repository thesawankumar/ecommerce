package thesawan.in.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thesawan.in.ecommerce.domain.AccountStatus;
import thesawan.in.ecommerce.model.Seller;
import thesawan.in.ecommerce.response.AdminProfileResponse;
import thesawan.in.ecommerce.service.AdminService;
import thesawan.in.ecommerce.service.SellerService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final SellerService sellerService;
    private final AdminService adminService;

    @PatchMapping("/seller/{id}/update-status/{status}")
    public ResponseEntity<Seller> updateSellerStatus(@PathVariable Long id, @PathVariable AccountStatus status) throws Exception {
        Seller updatedSeller = sellerService.updateSellerAccountStatus(id, status);
        return ResponseEntity.ok(updatedSeller);
    }

    @GetMapping("/sellers")
    public ResponseEntity<List<Seller>> getAllSellers(
            @RequestParam(required = false) AccountStatus status
    ) {
        List<Seller> sellers = sellerService.getAllSellers(status);
        return new ResponseEntity<>(sellers, HttpStatus.OK);
    }


    @GetMapping("/dashboard")
    public ResponseEntity<AdminProfileResponse> getAdminProfile() {
        AdminProfileResponse response = adminService.getAdminDashboardStats();
        return ResponseEntity.ok(response);
    }
}
