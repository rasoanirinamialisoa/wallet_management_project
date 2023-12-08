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

    public int getDebitAccountId() {
        return debitAccountId;
    }

    public void setDebitAccountId(int debitAccountId) {
        this.debitAccountId = debitAccountId;
    }

    public int getCreditAccountId() {
        return creditAccountId;
    }

    public void setCreditAccountId(int creditAccountId) {
        this.creditAccountId = creditAccountId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public LocalDateTime getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(LocalDateTime transferDate) {
        this.transferDate = transferDate;
    }

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
