package com.banking.microservice.accountservice.dto;

import com.banking.microservice.accountservice.enums.AccountType;
import com.banking.microservice.accountservice.enums.AccountStatus;
import lombok.*;

import java.math.BigDecimal;



@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDto {

    private Long id;

    private AccountType type;

    private Long customerId;

    private BigDecimal balance;

    private AccountStatus status;




}
