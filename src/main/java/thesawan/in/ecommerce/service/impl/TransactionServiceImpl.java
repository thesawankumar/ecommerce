package thesawan.in.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import thesawan.in.ecommerce.model.Order;
import thesawan.in.ecommerce.model.Seller;
import thesawan.in.ecommerce.model.Transaction;
import thesawan.in.ecommerce.repository.SellerRepository;
import thesawan.in.ecommerce.repository.TransactionRepository;
import thesawan.in.ecommerce.service.TransactionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;


    @Override
    public Transaction createTransaction(Order order) {

        Seller seller = sellerRepository.findById(order.getSellerId()).get();
        Transaction transaction = new Transaction();
        transaction.setOrder(order);
        transaction.setSeller(seller);
        transaction.setCustomer(order.getUser());
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsBySellerId(Seller seller) {
        return transactionRepository.findBySellerId(seller.getId());
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
