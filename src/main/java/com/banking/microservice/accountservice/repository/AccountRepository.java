package com.banking.microservice.accountservice.repository;

import com.banking.microservice.accountservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    Optional<Account> findByAccountNumber(String AccountNumber);

    boolean existsByAccountNumber(String AccountNumber);

}
