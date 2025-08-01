package thesawan.in.ecommerce.service;

import thesawan.in.ecommerce.model.Order;
import thesawan.in.ecommerce.model.Seller;
import thesawan.in.ecommerce.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Order order);

    List<Transaction> getTransactionsBySellerId(Seller seller);

    List<Transaction> getAllTransactions();
}
