package com.banking.microservice.accountservice.repository;

import com.banking.microservice.accountservice.entity.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select a from Account a where account.accountNumber= :accountNumber

            """)
    Optional<Account> findByAccountNumberForUpdate(String AccountNumber);

    boolean existsByAccountNumber(String AccountNumber);

    Optional<Account> findByAccountNumber(String accountNumber);



}
