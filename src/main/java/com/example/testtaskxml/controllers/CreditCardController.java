package com.example.testtaskxml.controllers;

import com.example.testtaskxml.DTO.CreditCardDto;
import com.example.testtaskxml.services.CreditCardService;
import com.example.testtaskxml.services.impl.CreditCardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;


@RestController
@RequestMapping("/testxml")
@RequiredArgsConstructor
public class CreditCardController {
    private final CreditCardService creditCardService;

    @GetMapping
    public List<CreditCardDto> getAllData(){
        List<CreditCardDto> cards=creditCardService.getAllData();

        return cards;
    }

    @GetMapping("/getCode")
    public ResponseEntity<String> getResult(){
        String code=creditCardService.encodeZipFile();
        if(code.isEmpty()) return (ResponseEntity<String>) ResponseEntity.noContent();
        return ResponseEntity.ok(code);
    }

}
