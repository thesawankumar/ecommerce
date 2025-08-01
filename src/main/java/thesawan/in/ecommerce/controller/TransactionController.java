package thesawan.in.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thesawan.in.ecommerce.model.Seller;
import thesawan.in.ecommerce.model.Transaction;
import thesawan.in.ecommerce.service.SellerService;
import thesawan.in.ecommerce.service.TransactionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final SellerService sellerService;


    @GetMapping("/seller")
    private ResponseEntity<List<Transaction>> getTransactionsBySeller(@RequestHeader("Authorization") String jwt) throws Exception {
        // Assuming the JWT contains the seller ID, you would typically decode it to get the seller
        Seller seller = sellerService.getSellerProfile(jwt);
        List<Transaction> transactions = transactionService.getTransactionsBySellerId(seller);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }


}
