package com.example.testtaskxml.controllers;

import com.example.testtaskxml.DTO.CreditCardDto;
import com.example.testtaskxml.DTO.SoapEnvelope;
import com.example.testtaskxml.services.impl.CreditCardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/testxml")
@RequiredArgsConstructor
public class CreditCardController {
    private final CreditCardServiceImpl creditCardService;

    @GetMapping
    public List<CreditCardDto> getAllData(){
        List<CreditCardDto> cards=creditCardService.getAllData();

        return cards;
    }

    @PostMapping
    public SoapEnvelope handleSoapRequest(@RequestBody SoapEnvelope soapEnvelope) {
        // Logic to handle the SOAP request goes here
        System.out.println(soapEnvelope);
        return soapEnvelope;
    }
}
