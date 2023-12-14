package org.example.model;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferHistoryEntry {
    private int TransferHistoryEntryId;
    private int debitTransactionId;
    private int creditTransactionId;
    private Double transferAmount;
    private Timestamp transferDate;

    public TransferHistoryEntry(int debitTransactionId, int creditTransactionId, double transferAmount, Timestamp transferDate) {
    }

}