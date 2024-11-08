package com.banking.account.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/bank")
public interface AccountController {

    @PostMapping("/withdraw")
    ResponseEntity<String> withdraw(@RequestParam("accountId") Long accountId, @RequestParam("amount") BigDecimal amount);

}
