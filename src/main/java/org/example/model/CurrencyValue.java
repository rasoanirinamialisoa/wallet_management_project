package org.example.model;


import java.math.BigDecimal;
import java.time.LocalDate;

public class CurrencyValue {
    private int id;
    private int sourceCurrencyId;
    private int destinationCurrencyId;
    private BigDecimal amount;
    private LocalDate effectiveDate;

    public CurrencyValue(int id, int sourceCurrencyId, int destinationCurrencyId, BigDecimal amount, LocalDate effectiveDate) {
        this.id = id;
        this.sourceCurrencyId = sourceCurrencyId;
        this.destinationCurrencyId = destinationCurrencyId;
        this.amount = amount;
        this.effectiveDate = effectiveDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSourceCurrencyId() {
        return sourceCurrencyId;
    }

    public void setSourceCurrencyId(int sourceCurrencyId) {
        this.sourceCurrencyId = sourceCurrencyId;
    }

    public int getDestinationCurrencyId() {
        return destinationCurrencyId;
    }

    public void setDestinationCurrencyId(int destinationCurrencyId) {
        this.destinationCurrencyId = destinationCurrencyId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Override
    public String toString() {
        return "CurrencyValue{" +
                "id=" + id +
                ", sourceCurrencyId=" + sourceCurrencyId +
                ", destinationCurrencyId=" + destinationCurrencyId +
                ", amount=" + amount +
                ", effectiveDate=" + effectiveDate +
                '}';
    }
}

