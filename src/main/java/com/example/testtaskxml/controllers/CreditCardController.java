package com.example.testtaskxml.controllers;

import com.example.testtaskxml.DTO.CreditCardDto;
import com.example.testtaskxml.services.CreditCardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/testxml")
public class CreditCardController {
    private final CreditCardService creditCardService;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @GetMapping
    public List<CreditCardDto> getAllData(){
        List<CreditCardDto> cards=creditCardService.getAllData();

        return cards;
    }
}
