package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransferHistoryEntry {
    private int debitAccountId;
    private int creditAccountId;
    private BigDecimal transferAmount;
    private LocalDateTime transferDate;

    public TransferHistoryEntry(int debitAccountId, int creditAccountId, BigDecimal transferAmount, LocalDateTime transferDate) {
        this.debitAccountId = debitAccountId;
        this.creditAccountId = creditAccountId;
        this.transferAmount = transferAmount;
        this.transferDate = transferDate;
    }

    // Ajoutez les getters et setters ici

    @Override
    public String toString() {
        return "TransferHistoryEntry{" +
                "debitAccountId=" + debitAccountId +
                ", creditAccountId=" + creditAccountId +
                ", transferAmount=" + transferAmount +
                ", transferDate=" + transferDate +
                '}';
    }
}

