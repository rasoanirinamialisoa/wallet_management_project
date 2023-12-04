package org.example;


import org.example.model.Account;
import org.example.model.Currency;
import org.example.model.Transaction;
import org.example.repository.AccountCrudOperations;
import org.example.repository.TransactionCrudOperations;
import org.example.repository.CurrencyCrudOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ConnectDatabase connectDatabase = new ConnectDatabase();
        Connection connection = null;

        try {
            connection = connectDatabase.openConnection();

            if (connection != null) {
                System.out.println("Connexion à la base de données réussie ok!");
            } else {
                System.out.println("La connexion à la base de données a échoué.");
            }

            // Utilisez la même instance de connexion créée au début
            AccountCrudOperations accountCrudOperations = new AccountCrudOperations(connection);

            // Tester la méthode findAll de AccountCrudOperations
            logger.info("Test de la méthode findAll de AccountCrudOperations");

            try {
                List<Account> allAccounts = accountCrudOperations.findAll();
                allAccounts.forEach(account -> logger.info("Compte trouvé : {}", account));
            } catch (Exception e) {
                logger.error("Une erreur s'est produite lors du test de la méthode findAll : {}", e.getMessage());
            }

            // Tester la méthode saveAll
            logger.info("Test de la méthode saveAll de AccountCrudOperations");

            // Créer la liste de comptes à sauvegarder
            List<Account> accountsToSave = new ArrayList<>();

            Account account1 = new Account();
            account1.setName("Compte 1");
            account1.setBalance(1000.0);
            account1.setCurrencyId(1); // Remplacez par l'ID réel de la devise
            account1.setAccountType("General");
            accountsToSave.add(account1);

            Account account2 = new Account();
            account2.setName("Compte 2");
            account2.setBalance(500.0);
            account2.setCurrencyId(2); // Remplacez par l'ID réel de la devise
            account2.setAccountType("Cash");
            accountsToSave.add(account2);

            // Appeler la méthode saveAll
            List<Account> savedAccounts = accountCrudOperations.saveAll(accountsToSave);

            // Journaliser les comptes sauvegardés
            savedAccounts.forEach(saved -> logger.info("Compte sauvegardé : {}", saved));

            // Tester la méthode save
            logger.info("Test de la méthode save de AccountCrudOperations");
            Account newAccount = new Account();
            newAccount.setName("Nouveau Compte");
            newAccount.setBalance(200.0);
            newAccount.setCurrencyId(3);
            newAccount.setAccountType("Credit Card");

            Account savedAccount = accountCrudOperations.save(newAccount);
            logger.info("Compte sauvegardé : {}", savedAccount);

            logger.info("Tests de AccountCrudOperations terminés!");

            // Utilisez la même instance de connexion créée au début
            TransactionCrudOperations transactionCrudOperations = new TransactionCrudOperations(connection);

// Tester la méthode findAll de TransactionCrudOperations
            logger.info("Test de la méthode findAll de TransactionCrudOperations");

            try {
                List<Transaction> allTransactions = transactionCrudOperations.findAll();
                allTransactions.forEach(transaction -> logger.info("Transaction trouvée : {}", transaction));
            } catch (Exception e) {
                logger.error("Une erreur s'est produite lors du test de la méthode findAll : {}", e.getMessage());
            }

// Tester la méthode saveAll
            logger.info("Test de la méthode saveAll de TransactionCrudOperations");

// Créer la liste de transactions à sauvegarder
            List<Transaction> transactionsToSave = new ArrayList<>();

            Transaction transaction1 = new Transaction();
            transaction1.setAccountId(7);
            transaction1.setDescription("Achat en ligne");
            transaction1.setAmount(50.0);
            transaction1.setDate(new Timestamp(System.currentTimeMillis()));
            transaction1.setType("DEBIT");
            transactionsToSave.add(transaction1);

            Transaction transaction2 = new Transaction();
            transaction2.setAccountId(5);
            transaction2.setDescription("Dépôt de salaire");
            transaction2.setAmount(2000.0);
            transaction1.setDate(new Timestamp(System.currentTimeMillis()));
            transaction2.setType("CREDIT");
            transactionsToSave.add(transaction2);

// Appeler la méthode saveAll
            List<Transaction> savedTransactions = transactionCrudOperations.saveAll(transactionsToSave);

// Journaliser les transactions sauvegardées
            savedTransactions.forEach(saved -> logger.info("Transaction sauvegardée : {}", saved));

// Tester la méthode save
            logger.info("Test de la méthode save de TransactionCrudOperations");
            Transaction newTransaction = new Transaction();
            newTransaction.setAccountId(9); // Remplacez par l'ID réel du compte
            newTransaction.setDescription("Nouvelle transaction");
            newTransaction.setAmount(100.0);
            newTransaction.setDate(new Timestamp(System.currentTimeMillis()));
            newTransaction.setType("DEBIT");

            Transaction savedTransaction = transactionCrudOperations.save(newTransaction);
            logger.info("Transaction sauvegardée : {}", savedTransaction);

            logger.info("Tests de TransactionCrudOperations terminés!");

            // Utilisez la même instance de connexion créée au début
            CurrencyCrudOperations currencyCrudOperations = new CurrencyCrudOperations(connection);

// Tester la méthode findAll de CurrencyCrudOperations
            logger.info("Test de la méthode findAll de CurrencyCrudOperations");

            try {
                List<Currency> allCurrencies = currencyCrudOperations.findAll();
                allCurrencies.forEach(currency -> logger.info("Devise trouvée : {}", currency));
            } catch (Exception e) {
                logger.error("Une erreur s'est produite lors du test de la méthode findAll : {}", e.getMessage());
            }

// Tester la méthode saveAll
            logger.info("Test de la méthode saveAll de CurrencyCrudOperations");

// Créer la liste de devises à sauvegarder
            List<Currency> currenciesToSave = new ArrayList<>();

            Currency currency1 = new Currency();
            currency1.setCurrencyCode("EUR");
            currency1.setCurrencyName("Euro");
            currenciesToSave.add(currency1);

            Currency currency2 = new Currency();
            currency2.setCurrencyCode("USD");
            currency2.setCurrencyName("Dollar américain");
            currenciesToSave.add(currency2);

// Appeler la méthode saveAll
            List<Currency> savedCurrencies = currencyCrudOperations.saveAll(currenciesToSave);

// Journaliser les devises sauvegardées
            savedCurrencies.forEach(saved -> logger.info("Devise sauvegardée : {}", saved));

// Tester la méthode save
            logger.info("Test de la méthode save de CurrencyCrudOperations");
            Currency newCurrency = new Currency();
            newCurrency.setCurrencyCode("GBP");
            newCurrency.setCurrencyName("Livre sterling");

            Currency savedCurrency = currencyCrudOperations.save(newCurrency);
            logger.info("Devise sauvegardée : {}", savedCurrency);


// Tester d'autres méthodes selon vos besoins...
            logger.info("Tests de CurrencyCrudOperations terminés!");



        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
