package com.banking.account.app.service;

import com.banking.account.app.dto.WithdrawalEvent;
import com.banking.account.app.entity.Account;
import com.banking.account.app.repository.AccountRepository;
import com.banking.account.app.util.ServiceException;
import com.banking.account.app.util.aws.SnsClientUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final SnsClientUtil snsClientUtil;

    private final AccountRepository accountRepository;

    public AccountServiceImpl(SnsClientUtil snsClientUtil, AccountRepository accountRepository) {
        this.snsClientUtil = snsClientUtil;
        this.accountRepository = accountRepository;
    }

    @Override
    public String withdraw(Long accountId, BigDecimal amount) {
        // add null-check using apache for multiple objects
        if (ObjectUtils.anyNull(accountId, amount)) {
            throw new ServiceException("Withdrawal failed, account Id OR amount field cannot be null.");
        }
        //opted to use hibernate for ORM and db client independence in case of future migrations
        Account account = accountRepository.findById(accountId).orElse(null);
        if (Objects.isNull(account)) {
            return "Withdrawal failed, account with account id: " + accountId + " does not exist";
        }
        // Process account withdrawal
        return processWithdrawal(amount, account);

    }

    private String processWithdrawal(BigDecimal amount, Account account) {
        BigDecimal currentBalance = account.getAmount();
        // check account balance
        if (Objects.nonNull(currentBalance) && currentBalance.compareTo(amount) >= 0) {
            // Update balance
            BigDecimal remainingBalance = currentBalance.subtract(amount);
            account.setAmount(remainingBalance);
            //save updated account object
            Account updatedAccount = accountRepository.save(account);
            return publish(updatedAccount);
        } else {
            // Insufficient funds
            return "Insufficient funds for withdrawal";
        }
    }

    private String publish(Account updatedAccount) {
        //publish withdrawal event to sns
        try {
            WithdrawalEvent withdrawalEvent = WithdrawalEvent.toWithdrawalEvent(updatedAccount);
            //import jackson library to convert object to a json string to publish to aws
            ObjectMapper objectMapper = new ObjectMapper();
            String message = objectMapper.writeValueAsString(withdrawalEvent);

            //log message
            log.info("publishing message {}", message);
            this.snsClientUtil.publish(message);
            return "Withdrawal Successful";
        } catch (JsonProcessingException e) {
            return "Failed to publish message: " + e.getMessage();
        }
    }
}
