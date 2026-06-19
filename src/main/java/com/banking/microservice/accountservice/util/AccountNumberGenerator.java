package com.banking.microservice.accountservice.util;

import java.util.Random;

public class AccountNumberGenerator {

    public static String generateAccountNumber(){
        Random random=new Random();
        StringBuilder accNum=new StringBuilder();

        for(int i=0;i<12;i++){
            accNum.append(random.nextInt(10));
        }
        return accNum.toString();
    }
}
