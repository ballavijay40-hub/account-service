package com.banking.microservice.accountservice.producer;


import com.banking.microservices.common.constants.KafkaTopics;
import org.springframework.kafka.core.KafkaTemplate;

import com.banking.microservices.common.events.result.TransactionResultEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionResultProducer {

    private final KafkaTemplate<String, TransactionResultEvent> kafkaTemplate;

    public void pubilsh(TransactionResultEvent event){
        kafkaTemplate.send(KafkaTopics.TRANSACTION_RESULTS,event.getReferenceNumber(),event);

    }



}
