package com.banking.microservice.accountservice.controller;


import com.banking.microservice.accountservice.dto.AccountRequestDto;
import com.banking.microservice.accountservice.dto.AccountResponseDto;
import com.banking.microservice.accountservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody AccountRequestDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(dto));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponseDto> getAccount(@PathVariable String accountNumber){
        return ResponseEntity.ok(accountService.getAccount(accountNumber));

    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponseDto> getBalance(@PathVariable String accountNumber){
        return ResponseEntity.ok(accountService.getBalance(accountNumber));
    }

    @PutMapping("/{accountNumber}")
    public ResponseEntity<Void> updateStatus(@PathVariable String accountNumber,@RequestParam String status){
        accountService.updateStatus(accountNumber,status)
        return ResponseEntity.noContent().build();
    }
}
