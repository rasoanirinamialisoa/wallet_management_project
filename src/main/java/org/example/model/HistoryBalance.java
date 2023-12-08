package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HistoryBalance {
    private LocalDateTime date;
    private BigDecimal balance ;

    public HistoryBalance(LocalDateTime date, BigDecimal balance) {
    }

    public void BalanceHistory(LocalDateTime date, BigDecimal balance) {
        this.date = date;
        this.balance = balance;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "BalanceHistory{" +
                "transactionDate=" + date +
                ", balance=" + balance +
                '}';
    }
}
