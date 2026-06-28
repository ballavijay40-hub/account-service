package com.banking.microservice.accountservice.dto;

import com.banking.microservices.common.enums.AccountStatus;
import com.banking.microservices.common.enums.AccountType;
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
