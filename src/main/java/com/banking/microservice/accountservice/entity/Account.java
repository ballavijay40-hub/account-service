package com.banking.microservice.accountservice.entity;

import com.banking.microservice.accountservice.enums.AccountStatus;
import com.banking.microservice.accountservice.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="accounts",
        uniqueConstraints={
        @UniqueConstraint(columnNames = {"accountNumber","custometId"})
},
        indexes = {
        @Index(name="idx_customer_id",columnList = "customerId")
        }
)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    @Column(nullable = false,precision = 19,scale=2)
    private BigDecimal balance=BigDecimal.ZERO;




    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AccountStatus status=AccountStatus.ACTIVE;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;



    @PrePersist
    public void onCreate(){
        this.createdAt=LocalDateTime.now();
        this.updatedAt= LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        this.updatedAt=LocalDateTime.now();
    }
}
