package com.banking.account.app.service;

import java.math.BigDecimal;

public interface AccountService {

    String withdraw(Long accountId, BigDecimal amount);
}
