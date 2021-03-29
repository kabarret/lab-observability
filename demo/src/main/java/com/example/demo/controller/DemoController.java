package com.example.demo.controller;

import com.example.demo.model.BuyRequest;
import com.example.demo.model.DemoData;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.prometheus.client.Histogram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@RestController
public class DemoController {
   
    @RequestMapping("/test")
    public String testGet(){
        return "Test OK";
    }

    @RequestMapping("/random")
    public ResponseEntity random(){
        Random random = new Random();
        int randomNumber = random.nextInt(10);
        if (randomNumber <= 5){
            return new ResponseEntity<>(new DemoData("process Ok"), HttpStatus.OK);
        } else if ( randomNumber <= 8){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping("/performance")
    public ResponseEntity performance() throws NoSuchAlgorithmException, InterruptedException {
        Random random = new Random();
        String hashText = "";
        int randomNumber = random.nextInt(1000);
        byte[] randomBytes = new byte[10];
        random.nextBytes(randomBytes);
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(randomBytes);
        BigInteger bigInt = new BigInteger(1,md5.digest());
        hashText = bigInt.toString(16);
        System.out.println(hashText);
        Thread.sleep(randomNumber);
        return new ResponseEntity<>(new DemoData(hashText), HttpStatus.OK);
    }

    @Autowired
    private MeterRegistry meterRegistry;
    private DistributionSummary amountValueSpend;
    @PostConstruct
    public void init(){
        amountValueSpend = DistributionSummary
                .builder("buy.value")
                .description("the amount value spend") // optional
                .register(meterRegistry);
    }

    @PostMapping("/buy")
    public ResponseEntity buy(@RequestBody BuyRequest buyRequest){
        amountValueSpend.record(buyRequest.getValue());
        System.out.println("recoded value: " + buyRequest.getValue());
        return new ResponseEntity(buyRequest, HttpStatus.OK);
    }
}
