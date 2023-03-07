package com.example.testtaskxml.services;

import com.example.testtaskxml.DTO.CreditCardDto;
import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface CreditCardService {
    Document createXmlFile() throws Exception;
    ByteArrayOutputStream xmlToZipFile();
    String encodeZipFile();
    List<CreditCardDto> getAllData();
}
