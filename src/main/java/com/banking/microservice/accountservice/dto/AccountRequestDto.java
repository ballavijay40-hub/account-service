package com.banking.microservice.accountservice.dto;



import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDto {

    @NotNull(message="customer id is required.")
    private Long customerId;

    @NotNull(message="account type is required.")
    private AccountType accountType;

    @DecimalMin(value="0.0",inclusive=true,message="initial deposit must not be negative")
    private BigDecimal initialDeposit;





}
