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

    public HistoryBalance(LocalDateTime date, Double balance) {
    }
}
