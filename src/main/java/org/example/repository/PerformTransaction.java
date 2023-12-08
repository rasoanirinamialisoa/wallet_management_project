
public class PerformTransaction {
        private AccountCrudOperation accountCrudOperation;

        public  PerformTransaction(AccountCrudOperation accountCrudOperation) {
        this.accountCrudOperation = accountCrudOperation;
    }

    public void performTransaction(Account account, double amount, String description, String transactionType) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(account.getAccountId());
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setDate(new Timestamp(System.currentTimeMillis()));
        transaction.setType(transactionType);

        if (account.getTransactions() == null) {
            account.setTransactions(new ArrayList<>());
        }
        account.getTransactions().add(transaction);

        if ("credit".equalsIgnoreCase(transactionType)) {
            account.setBalance(account.getBalance() + amount);
        } else if ("debit".equalsIgnoreCase(transactionType)) {
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
        
        if (accountCrudOperation != null) {
            accountCrudOperation.saveBalance(account.getAccountId(), account.getBalance());
        }
    }
}


