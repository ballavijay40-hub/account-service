package com.banking.microservice.accountservice.service;


import com.banking.microservice.accountservice.dto.AccountRequestDto;
import com.banking.microservice.accountservice.dto.AccountResponseDto;
import com.banking.microservice.accountservice.entity.Account;

import com.banking.microservice.accountservice.exception.AccountNotFoundException;
import com.banking.microservice.accountservice.exception.InsufficientBalanceException;
import com.banking.microservice.accountservice.exception.InvalidAccountStatusException;
import com.banking.microservice.accountservice.producer.TransactionResultProducer;
import com.banking.microservice.accountservice.repository.AccountRepository;
import com.banking.microservice.accountservice.util.AccountNumberGenerator;
import com.banking.microservices.common.enums.AccountStatus;
import com.banking.microservices.common.enums.TransactionStatus;
import com.banking.microservices.common.events.request.TransactionRequestEvent;
import com.banking.microservices.common.events.result.TransactionResultEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionResultProducer transactionResultProducer;

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
        try {
            AccountStatus s = AccountStatus.valueOf(status);
            account.setStatus(s);
            accountRepository.save(account);
        } catch (IllegalArgumentException e) {
            throw new InvalidAccountStatusException(
                    "Invalid account status: " + status
            );
        }




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


    @Transactional
    @Override
    public void processTransaction(TransactionRequestEvent transactionRequestEvent){
//        TransactionResultEvent.TransactionResultEventBuilder transactionResultEventBuilder=TransactionResultEvent.builder()
//                .referenceNumber(transactionRequestEvent.getReferenceNumber());

        TransactionResultEvent transactionResultEvent=new TransactionResultEvent();
        transactionResultEvent.setReferenceNumber(transactionRequestEvent.getReferenceNumber());


        try{
            switch (transactionRequestEvent.getTransactionType()){
                case DEPOSIT -> processDepositTransaction(transactionRequestEvent);
                case WITHDRAW -> processWithdrawTransaction(transactionRequestEvent);
                case TRANSFER -> processTransferTransaction(transactionRequestEvent);
                default -> throw new IllegalArgumentException("Unsupported transaction type.");
            }
            transactionResultEvent.setStatus(TransactionStatus.SUCCESS);

        }catch(Exception ex){
            log.error(
                    "Transaction {} failed.",
                    transactionRequestEvent.getReferenceNumber(),
                    ex
            );
            transactionResultEvent.setStatus(TransactionStatus.FAILED);
            transactionResultEvent.setFailureReason(ex.getMessage());

        }

        transactionResultProducer.publish(transactionResultEvent);
    }

    private  void processDepositTransaction(TransactionRequestEvent transactionRequestEvent){
        //load acc
        Account account=getAccountForUpdate(transactionRequestEvent.getFromAccountNumber());
        //validate acc
        validateAccount(account);
        //deposit
        credit(account,transactionRequestEvent.getAmount());
        //save to db
//        accountRepository.save(account);

        //logs
        log.info("Deposit complete for account {}.",account.getAccountNumber());



    }


    private void processWithdrawTransaction(TransactionRequestEvent transactionRequestEvent){
        //load acc
        Account account=getAccountForUpdate(transactionRequestEvent.getFromAccountNumber());
        //validate
        validateAccount(account);
        //withdraw
        debit(account,transactionRequestEvent.getAmount());
        //save
//        accountRepository.save(account);

        //log
        log.info("withdraw completed for acc {}",account.getAccountNumber());
    }

    private void processTransferTransaction(TransactionRequestEvent transactionRequestEvent){
        if(transactionRequestEvent.getFromAccountNumber().equals(transactionRequestEvent.getToAccountNumber())){
            throw new IllegalArgumentException("Source and destination accounts cannot be the same.");
        }

        //load acc's
        Account fromAcc=getAccountForUpdate(transactionRequestEvent.getFromAccountNumber());
        Account toAcc=getAccountForUpdate(transactionRequestEvent.getToAccountNumber());

        //validate acc's

        validateAccount(fromAcc);
        validateAccount(toAcc);

        //now transfer
        transfer(fromAcc,toAcc,transactionRequestEvent.getAmount());

//        accountRepository.save(fromAcc);
//        accountRepository.save(toAcc);

        log.info("transaction completed from Account {} ,to Account {}.",fromAcc.getAccountNumber(),toAcc.getAccountNumber());


    }

//====================================================================

    //helper methods

    private void transfer(Account fromAcc,Account toAcc,BigDecimal amount){
        //debit from fromAcc

        debit(fromAcc,amount);

        //credit to toACC

        credit(toAcc,amount);


    }







    //load acc
    private Account getAccountForUpdate(String accountNumber){
        return accountRepository.findByAccountNumberForUpdate(accountNumber)
                .orElseThrow(()->new AccountNotFoundException("Account  not found."));
    }

    //check acc status
    private void validateAccount(Account account){
        if(account.getStatus()!=AccountStatus.ACTIVE){
            throw new InvalidAccountStatusException("Account is not active:"+account.getAccountNumber());
        }

    }

    //debt
    private void debit(Account account, BigDecimal amount){


        if(amount.compareTo(BigDecimal.ZERO)<=0){
            throw  new IllegalArgumentException("Amount must be greater than 0.");
        }

        if(account.getBalance().compareTo(amount)<0){
            throw  new InsufficientBalanceException("Insufficient balance.");
        }

        account.setBalance(account.getBalance().subtract(amount));

    }

    //credit
    private void credit(Account account,BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }

        account.setBalance(account.getBalance().add(amount));
    }






}
