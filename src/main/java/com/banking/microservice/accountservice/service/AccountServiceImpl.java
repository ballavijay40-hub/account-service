package com.banking.microservice.accountservice.service;


import com.banking.microservice.accountservice.dto.AccountRequestDto;
import com.banking.microservice.accountservice.dto.AccountResponseDto;
import com.banking.microservice.accountservice.entity.Account;
import com.banking.microservice.accountservice.enums.AccountStatus;
import com.banking.microservice.accountservice.exception.AccountNotFoundException;
import com.banking.microservice.accountservice.repository.AccountRepository;
import com.banking.microservice.accountservice.util.AccountNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountResponseDto createAccount(AccountRequestDto dto){


        Account account=Account.builder()
                .accountNumber(AccountNumberGenerator.generateAccountNumber())
                .customerId(dto.getCustomerId())
                .type(dto.getAccountType())
                .balance(dto.getInitialDeposit())
                .status(AccountStatus.ACTIVE)

                .build();

        accountRepository.save(account);

        return toResponse(account);
    }


    @Override
    public AccountResponseDto getAccount(String accountNumber){
        Account account=accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()->new AccountNotFoundException("Account not found."));

        return toResponse(account);

    }

    @Override
    public AccountResponseDto getBalance(String accountNumber){
        Account account=accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()->new AccountNotFoundException("Account not found."));

        return toResponse(account);


    }

    @Override
    public void updateStatus(String accountNumber,String status){
        Account account=accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()->new AccountNotFoundException("Account not found."));

        account.setStatus(AccountStatus.valueOf(status));
        accountRepository.save(account);


    }


    public AccountResponseDto toResponse(Account account){
        return AccountResponseDto.builder()
                .id(account.getId())
                .type(account.getType())
                .customerId(account.getCustomerId())
                .balance(account.getBalance())
                .status(account.getStatus())
                .build();

    }



}
