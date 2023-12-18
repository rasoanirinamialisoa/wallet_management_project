package org.example.model;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HistoryBalance {
    private int historyId;
    private Timestamp date;
    private double balance;

    public static final String HISTORY_ID = "historyId";
    public static final String DATE_HISTORY_BALANCE = "date";
    public static final String BALANCE_HISTORY = "balance";
    public HistoryBalance(LocalDateTime date, Double balance) {
    }
}
