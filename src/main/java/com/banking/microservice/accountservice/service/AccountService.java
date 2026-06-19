package com.banking.microservice.accountservice.service;

import com.banking.microservice.accountservice.dto.AccountRequestDto;
import com.banking.microservice.accountservice.dto.AccountResponseDto;

public interface AccountService {


    AccountResponseDto createAccount(AccountRequestDto dto);

    AccountResponseDto getAccount(String accountNumber);

    AccountResponseDto getBalance(String accountNumber);

    void updateStatus(String accountNumber,String status);
}
