package org.example;


import org.example.model.*;
import org.example.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

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
            account1.setName("Current account");
            account1.setBalance(1000.0);
            account1.setCurrencyId(1); // Remplacez par l'ID réel de la devise
            account1.setAccountType("Bank");
            accountsToSave.add(account1);

            Account account2 = new Account();
            account2.setName("Savings account");
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
            newAccount.setName("Current account");
            newAccount.setBalance(200.0);
            newAccount.setAccountType("Bank");
            newAccount.setCurrencyId(3);

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
            transaction1.setAccountId(1);
            transaction1.setLabelTransaction("Achat en ligne");
            transaction1.setAmount(50.0);
            transaction1.setDateOfTransaction(Timestamp.valueOf(LocalDateTime.now()));
            transaction1.setTransactionsType("Debit");
            transactionsToSave.add(transaction1);

            Transaction transaction2 = new Transaction();
            transaction2.setAccountId(3);
            transaction2.setLabelTransaction("Dépôt de salaire");
            transaction2.setAmount(200000.0);
            transaction2.setDateOfTransaction(Timestamp.valueOf(LocalDateTime.now()));
            transaction2.setTransactionsType("Credit");
            transaction2.setCategoryId(7);
            transactionsToSave.add(transaction2);

// Appeler la méthode saveAll
            List<Transaction> savedTransactions = transactionCrudOperations.saveAll(transactionsToSave);

// Journaliser les transactions sauvegardées
            savedTransactions.forEach(saved -> logger.info("Transaction sauvegardée : {}", saved));

// Tester la méthode save
            logger.info("Test de la méthode save de TransactionCrudOperations");
            Transaction newTransaction = new Transaction();
            newTransaction.setAccountId(2); // Remplacez par l'ID réel du compte
            newTransaction.setLabelTransaction("Nouvelle transaction");
            newTransaction.setAmount(100000.0);
            newTransaction.setDateOfTransaction(Timestamp.valueOf(LocalDateTime.now()));
            newTransaction.setTransactionsType("Debit");
            newTransaction.setCategoryId(2);

            Transaction savedTransaction = transactionCrudOperations.save(newTransaction);
            logger.info("Transaction sauvegardée : {}", savedTransaction);

            BalanceSumsResult balanceSumsResult = transactionCrudOperations.getBalanceSumBetweenDates(
                    22, LocalDateTime.parse("2023-01-01T00:00:00"), LocalDateTime.parse("2023-12-31T23:59:59"));

            // Affichez les résultats dans les logs
            logger.info("Total Income: {}", balanceSumsResult.getTotalIncome());
            logger.info("Total Expense: {}", balanceSumsResult.getTotalExpense());

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
            currency2.setCurrencyCode("MGA");
            currency2.setCurrencyName("Ariary");
            currenciesToSave.add(currency2);

// Appeler la méthode saveAll
            List<Currency> savedCurrencies = currencyCrudOperations.saveAll(currenciesToSave);

// Journaliser les devises sauvegardées
            savedCurrencies.forEach(saved -> logger.info("Devise sauvegardée : {}", saved));

// Tester la méthode save
            logger.info("Test de la méthode save de CurrencyCrudOperations");
            Currency newCurrency = new Currency();
            newCurrency.setCurrencyCode("MGA");
            newCurrency.setCurrencyName("Ariary");

            Currency savedCurrency = currencyCrudOperations.save(newCurrency);
            logger.info("Devise sauvegardée : {}", savedCurrency);

            logger.info("Tests de CurrencyCrudOperations terminés!");


            TransferHistoryOperations transferHistoryOperations = new TransferHistoryOperations(connection);
            logger.info("Test de la méthode findAll de TransferHistoryOperations");

            logger.info("Test de la méthode findAll de TransferHistoryOperations");

            try {
                LocalDateTime startDate = LocalDateTime.parse("2023-01-01T00:00:00");
                LocalDateTime endDate = LocalDateTime.parse("2023-12-31T23:59:59");

                List<TransferHistoryEntry> allTransferHistoryOperations = TransferHistoryOperations.findAll(startDate, endDate);

                if (!allTransferHistoryOperations.isEmpty()) {
                    for (TransferHistoryEntry entry : allTransferHistoryOperations) {
                        logger.info("Historique de transfert trouvé : {}", entry);
                    }
                } else {
                    logger.info("Aucun historique de transfert trouvé.");
                }
            } catch (Exception e) {
                logger.error("Une erreur s'est produite lors du test de la méthode findAll : {}", e.getMessage());
            }

            HistoryCrudOperation historyCrudOperation = new HistoryCrudOperation(connection);
            logger.info("Test de la méthode HistoryCrudOperation");
            try {
                LocalDateTime startDate = LocalDateTime.parse("2023-01-01T00:00:00");
                LocalDateTime endDate = LocalDateTime.parse("2023-12-31T23:59:59");
                String accountId = "42"; // Remplacez par l'ID réel du compte

                List<HistoryBalance> allBalanceHistory = HistoryCrudOperation.findAll(accountId, startDate, endDate);


                if (!allBalanceHistory.isEmpty()) {
                    for (HistoryBalance entry : allBalanceHistory) {
                        logger.info("Historique de solde trouvé : {}", entry);
                    }
                } else {
                    logger.info("Aucun historique de solde trouvé.");
                }
            } catch (Exception e) {
                logger.error("Une erreur s'est produite lors du test de la méthode findAll : {}", e.getMessage());
            }

            AccountBalanceRepository accountBalanceRepository = new AccountBalanceRepository(connection);
            logger.info("Test de la méthode AccountBalanceRepository");
            try {
                String accountId = "45"; // Remplacez par l'ID réel du compte

                List<TransferHistoryEntry> allTransferHistoryEntry = accountBalanceRepository.findAll(Integer.parseInt(accountId));

                if (!allTransferHistoryEntry.isEmpty()) {
                    for (TransferHistoryEntry entry : allTransferHistoryEntry) {
                        logger.info("Historique de transfert trouvé : {}", entry);
                    }
                } else {
                    logger.info("Aucun historique de transfert trouvé.");
                }
            } catch (Exception e) {
                logger.error("Une erreur s'est produite lors du test de la méthode findAll de AccountBalanceRepository : {}", e.getMessage());
            }

            // Test de la méthode save de AccountBalanceRepository
            logger.info("Test de la méthode save de AccountBalanceRepository");

            try {
                int accountId = 8;
                double balanceToAdd = 3000000.0;

                Account updatedAccount = accountBalanceRepository.save(accountId, balanceToAdd);

                if (updatedAccount != null) {
                    logger.info("Solde du compte mis à jour. Compte ID : {}, Montant ajouté : {}", updatedAccount.getAccountId(), balanceToAdd);
                } else {
                    logger.warn("Aucune mise à jour effectuée. Compte ID : {}, Montant ajouté : {}", accountId, balanceToAdd);
                }
            } catch (SQLException e) {
                logger.error("Erreur lors de la mise à jour du solde du compte : {}", e.getMessage());
            }

            // tester la méthode saveTransferHistory
            logger.info("Test de la méthode saveTransferHistory de AccountBalanceRepository");
            try {
                int debitAccountId = 1;
                int creditAccountId = 2;
                double amount = 100.0;
                LocalDateTime transferDate = LocalDateTime.now();

                accountBalanceRepository.saveTransferHistory(debitAccountId, creditAccountId, amount, transferDate);

                logger.info("Historique de transfert enregistré avec succès. Debit Account ID : {}, Credit Account ID : {}, Montant : {}, Date de transfert : {}", debitAccountId, creditAccountId, amount, transferDate);
            } catch (SQLException e) {
                logger.error("Erreur lors de l'enregistrement de l'historique de transfert : {}", e.getMessage());
            }

            // Test de la méthode getExchangeRate de AccountExchangeRate
            AccountExchangeRate accountExchangeRate = new AccountExchangeRate(connection);
            logger.info("Test de la méthode getExchangeRate de AccounteExchangeRate");

            int currencyId = 6;
            LocalDateTime date = LocalDateTime.now();
            AccountExchangeRate.ExchangeRateCalculationType calculationType = AccountExchangeRate.ExchangeRateCalculationType.WEIGHTED_AVERAGE;

            double exchangeRate = accountExchangeRate.getExchangeRate(date, currencyId, calculationType);

            logger.info( "Taux de change récupéré avec succès. Devise ID : {}, Date : {}, Taux : {}", currencyId, date, exchangeRate);


            // Utilisez la même instance de connexion créée au début
            CategoryCrudOperations categoryCrudOperations = new CategoryCrudOperations(connection);

            // Tester la méthode findAll de CategoryCrudOperations
            logger.info("Test de la méthode findAll de CategoryCrudOperations");

            try {
                List<Category> allCategories = categoryCrudOperations.findAll();
                allCategories.forEach(category -> logger.info("Catégorie trouvée : {}", category));
            } catch (Exception e) {
                logger.error("Une erreur s'est produite lors du test de la méthode findAll : {}", e.getMessage());
            }

            // Tester la méthode saveAll
            logger.info("Test de la méthode saveAll de CategoryCrudOperations");

            // Créer la liste de catégories à sauvegarder
            List<Category> categoriesToSave = new ArrayList<>();

            Category category1 = new Category();
            category1.setCategoryName("Voyage");
            categoriesToSave.add(category1);

            Category category2 = new Category();
            category2.setCategoryName("Etude");
            categoriesToSave.add(category2);

            // Appeler la méthode saveAll
            List<Category> savedCategories = categoryCrudOperations.saveAll(categoriesToSave);

            // Journaliser les catégories sauvegardées
            savedCategories.forEach(saved -> logger.info("Catégorie sauvegardée : {}", saved));

            // Tester la méthode save
            logger.info("Test de la méthode save de CategoryCrudOperations");
            Category newCategory = new Category();
            newCategory.setCategoryName("Paris sport");

            Category savedCategory = categoryCrudOperations.save(newCategory);
            logger.info("Catégorie sauvegardée : {}", savedCategory);

            logger.info("Tests de CategoryCrudOperations terminés!");



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
