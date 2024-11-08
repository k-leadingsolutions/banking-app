package com.banking.account.app.dto;

import com.banking.account.app.entity.Account;
import lombok.*;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalEvent  {

    private Long accountId;
    private BigDecimal amount;
    private String status;

    public static WithdrawalEvent toWithdrawalEvent(Account account) {
        return builder()
                .accountId(account.getId())
                .amount(account.getAmount())
                .status(account.getStatus())
                .build();
    }
}
