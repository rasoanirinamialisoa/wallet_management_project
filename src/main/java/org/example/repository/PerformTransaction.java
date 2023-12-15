package org.example.repository;
import org.example.model.Account;
import org.example.model.Transaction;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
public class PerformTransaction {
        private AccountCrudOperations accountCrudOperations;

        public  PerformTransaction(AccountCrudOperations accountCrudOperations) {
        this.accountCrudOperations = accountCrudOperations;
    }

    public void performTransaction(Account account, double amount, String labelTransaction, String transactionsType) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(account.getAccountId());
        transaction.setLabelTransaction(labelTransaction);
        transaction.setAmount(amount);
        transaction.setTransactionsType(transactionsType);

        if (account.getTransactions() == null) {
            account.setTransactions(new ArrayList<>());
        }
        account.getTransactions().add(transaction);

        if ("credit".equalsIgnoreCase(transactionsType)) {
            account.setBalance(account.getBalance() + amount);
        } else if ("debit".equalsIgnoreCase(transactionsType)) {
            if (amount > account.getBalance()) {
                throw new IllegalArgumentException("Insufficient funds for debit transaction");
            }
            account.setBalance(account.getBalance() - amount);
        } else {
            throw new IllegalArgumentException("Invalid transaction type");
        }

        saveBalance(account);
    }
    private void saveBalance(Account account) {
        
        if (accountCrudOperations != null) {
            accountCrudOperations.saveBalance(account.getAccountId(), account.getBalance());
        }
    }
}


