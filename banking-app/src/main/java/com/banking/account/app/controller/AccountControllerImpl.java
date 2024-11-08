package com.banking.account.app.controller;

import com.banking.account.app.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountControllerImpl implements AccountController {

    private final AccountService accountService;

    public AccountControllerImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public ResponseEntity<String> withdraw(Long accountId, BigDecimal amount) {
        return ResponseEntity.ok(accountService.withdraw(accountId, amount));
    }

}