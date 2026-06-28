package com.banking.microservice.accountservice.consumer;

import com.banking.microservice.accountservice.service.AccountService;
import com.banking.microservices.common.constants.KafkaTopics;
import com.banking.microservices.common.events.request.TransactionRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionRequestConsumer {

    private final AccountService accountService;

    @KafkaListener(
            topics= KafkaTopics.TRANSACTION_REQUESTS,
            groupId="acount-group"
    )
    public void consume(TransactionRequestEvent transactionRequestEvent){
        log.info("Received trasaction {}.",transactionRequestEvent.getReferenceNumber());
        accountService.processTransaction(transactionRequestEvent);
    }


}
